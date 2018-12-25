package com.target.dealbrowserpoc.dealbrowser.util;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * A {@link JsonAdapter.Factory} to handle polymorphic deserialization.
 */
public final class RuntimeTypeJsonAdapterFactory implements JsonAdapter.Factory {
    /**
     * Type-safe way to compare the {@link }.
     * This way you can pass the enum to be compared in the decision statement in the
     * receiving Fragment.
     */

    //TODO: Adapted/Update as necessary
    public enum JsonContentType {
        AD("ad"),
        NEWS("news", "events", "video", "contests"),
        PODCAST("podcast"),
        STREAM("stream"),
        UTILITIES("utilities");

        String[] dataTypes;

        JsonContentType(String... types) {
            dataTypes = types;
        }

        public String getDataType() {
            return dataTypes[0];
        }

        public boolean isDataType(String typeString) {
            for (String dataType : dataTypes) {
                if (dataType.equalsIgnoreCase(typeString))
                    return true;
            }
            return false;
        }

        public <X> void addSubtypeToBuilder(RuntimeType.Builder<X> builder, Class<? extends X> subtype) {
            for (String dataType : dataTypes)
                builder.withSubtype(subtype, dataType);
        }
    }

    private final Map<Class<?>, RuntimeType> baseTypeToRuntimeType = new LinkedHashMap<>();

    public RuntimeTypeJsonAdapterFactory registerRuntimeType(RuntimeType type) {
        baseTypeToRuntimeType.put(type.baseType, type);
        return this;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public JsonAdapter<?> create(@NonNull Type type, Set<? extends Annotation> annotations, @NonNull Moshi moshi) {
        if (!annotations.isEmpty()) {
            return null;
        }

        final RuntimeType runtimeType = baseTypeToRuntimeType.get(Types.getRawType(type));

        if (runtimeType == null) {
            return null;
        }

        final Map<String, JsonAdapter<Object>> discriminatorValueToJsonAdapter =
                new LinkedHashMap<>(runtimeType.discriminatorValueToSubtype.size());

        for (String key : runtimeType.discriminatorValueToSubtype.keySet()) {
            discriminatorValueToJsonAdapter.put(key,
                    moshi.adapter(runtimeType.discriminatorValueToSubtype.get(key), annotations));
        }
        return new RuntimeTypeJsonAdapter(runtimeType, discriminatorValueToJsonAdapter, moshi);
    }

    /**
     * Encapsulates all the information needed to
     * identify a polymorphic deserialization case.
     */
    public static class RuntimeType {

        /**
         * The Json key whose value will determine
         * which type of object is.
         */
        public final String discriminatorKey;

        /**
         * The base class that every subtype extends.
         * This class will contain a field to store the
         * discriminator value, as well as some other
         * common properties.
         */
        public final Class<?> baseType;

        /**
         * A map that defines a subtype for each discriminator value.
         */
        public final Map<String, Class<?>> discriminatorValueToSubtype;

        /**
         * A map that defines a label for each discriminator subtype.
         */
        public final Map<Class<?>, String> discriminatorSubtypeToLabel;

        private <T> RuntimeType(Builder<T> builder) {
            this.discriminatorKey = builder.discriminatorKey;
            this.baseType = builder.baseType;
            this.discriminatorValueToSubtype = Collections.unmodifiableMap(builder.discriminatorValueToSubtype);
            this.discriminatorSubtypeToLabel = Collections.unmodifiableMap(builder.discriminatorSubtypeToLabel);
        }

        public static <T> RuntimeType.Builder<T> of(Class<T> baseType, String discriminatorKey) {
            return new Builder<>(baseType, discriminatorKey);
        }

        public static class Builder<T> {

            private String discriminatorKey;
            private Class<T> baseType;
            private Map<String, Class<?>> discriminatorValueToSubtype = new LinkedHashMap<>();
            private Map<Class<?>, String> discriminatorSubtypeToLabel = new LinkedHashMap<>();

            private Builder(Class<T> baseType, String discriminatorKey) {
                this.baseType = baseType;
                this.discriminatorKey = discriminatorKey;
            }

            /**
             * Stores the {{@code discriminatorValue}, {@code subtype}} pair each time
             * that gets called.
             */
            public Builder<T> withSubtype(Class<? extends T> subtype, String discriminatorValue) {
                discriminatorValueToSubtype.put(discriminatorValue, subtype);
                discriminatorSubtypeToLabel.put(subtype, discriminatorValue);
                return this;
            }

            public RuntimeType build() {
                if (discriminatorKey == null) {
                    throw new IllegalArgumentException("discriminatorKey cannot be null");
                }

                if (discriminatorKey.isEmpty()) {
                    throw new IllegalArgumentException("discriminatorKey cannot be empty");
                }

                if (baseType == null) {
                    throw new IllegalArgumentException("baseType cannot be null");
                }

                return new RuntimeType(this);
            }
        }
    }

    private static class RuntimeTypeJsonAdapter extends JsonAdapter<Object> {

        private final RuntimeType runtimeType;
        private final Map<String, JsonAdapter<Object>> discriminatorValueToJsonAdapter;

        private final Map<Class<?>, JsonAdapter<?>> subtypeToDelegate;
        private final Map<Class<?>, String> subtypeToLabel;
        private final JsonAdapter<Map<String, Object>> toJsonDelegate;

        private RuntimeTypeJsonAdapter(RuntimeType runtimeType, Map<String, JsonAdapter<Object>> discriminatorValueToJsonAdapter, Moshi moshi) {
            this.runtimeType = runtimeType;
            this.discriminatorValueToJsonAdapter = discriminatorValueToJsonAdapter;
            this.toJsonDelegate =
                    moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));

            this.subtypeToLabel = new LinkedHashMap<>(runtimeType.discriminatorSubtypeToLabel);
            this.subtypeToDelegate =
                    new LinkedHashMap<>(runtimeType.discriminatorValueToSubtype.size());
        }

        @Override
        public Object fromJson(@NonNull JsonReader reader) throws IOException {
            Object raw = reader.readJsonValue();

            if (!(raw instanceof Map) && null != raw) {
                throw new JsonDataException(
                    "Value must be a JSON object but had a value of " + raw +
                            " of type " + raw.getClass());
            }

            @SuppressWarnings("unchecked") // This is a JSON object.
                    Map<String, Object> value = (Map<String, Object>) raw;

            Object label = value.get(runtimeType.discriminatorKey);
            if (label == null) {
                throw new JsonDataException("Missing label for " + runtimeType.discriminatorKey);
            }
            if (!(label instanceof String)) {
                throw new JsonDataException("Label for "
                        + runtimeType.discriminatorKey
                        + " must be a string but had a value of "
                        + label
                        + " of type "
                        + label.getClass());
            }
            JsonAdapter<?> delegate = discriminatorValueToJsonAdapter.get(label);
            if (delegate == null) {
                throw new JsonDataException("Type not registered for label: " + label);
            }
            return delegate.fromJsonValue(value);
        }

        @Override
        public void toJson(@NonNull JsonWriter writer, Object value) throws IOException {
            Class<?> subtype = value.getClass();

            @SuppressWarnings("unchecked") // The delegate is a JsonAdapter<subtype>.
            JsonAdapter<Object> delegate = (JsonAdapter<Object>) subtypeToDelegate.get(subtype);

            if (delegate == null) {
                throw new JsonDataException("Type not registered: " + subtype);
            }

            @SuppressWarnings("unchecked") // This is a JSON object.
                    Map<String, Object> jsonValue = (Map<String, Object>) delegate.toJsonValue(value);

            Object existingLabel = jsonValue.put(runtimeType.discriminatorKey, subtypeToLabel.get(subtype));

            if (existingLabel != null) {
                throw new JsonDataException(
                        "Label field " + runtimeType.discriminatorKey + " already defined as " + existingLabel);
            }

            toJsonDelegate.toJson(writer, jsonValue);
        }
    }
}
