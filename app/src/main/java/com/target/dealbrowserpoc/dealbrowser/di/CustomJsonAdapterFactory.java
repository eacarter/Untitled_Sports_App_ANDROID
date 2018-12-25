package com.target.dealbrowserpoc.dealbrowser.di;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * If model classes require a custom JsonAdapter add it in here.
 */
class CustomJsonAdapterFactory implements JsonAdapter.Factory {
    @Nullable
    @Override
    public JsonAdapter<?> create(@NonNull Type type, @NonNull Set<? extends Annotation> annotations, @NonNull Moshi moshi) {
        if (!annotations.isEmpty())
            return null;
//        if (type.equals(MyClass.class)) {
//            return new MyClass.CustomMoshiJsonAdapter(moshi);
//        }
        return null;
    }
}
