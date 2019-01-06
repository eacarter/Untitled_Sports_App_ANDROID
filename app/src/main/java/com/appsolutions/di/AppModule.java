package com.appsolutions.di;

import android.content.Context;

import com.appsolutions.LibApplication;
import com.appsolutions.util.EncodingInterceptor;
import com.appsolutions.util.RuntimeTypeJsonAdapterFactory;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import java.net.CookieHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.lifecycle.MutableLiveData;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

//TODO: Remove if not using any of the following
//import com.jacapps.bobandtom.library.util.TLSSocketFactory;
//import com.crashlytics.android.Crashlytics;
//import com.google.firebase.analytics.FirebaseAnalytics;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Provides @Singleton
    Context provideContext(LibApplication application) {
        return application;
    }

    @Provides
    @Singleton
    @Named("mainActivityRunning")
    MutableLiveData<Boolean> provideMainActivityRunning() {
        return new MutableLiveData<>();
    }

    @Provides @Singleton
    Moshi provideMoshi() {
        return new Moshi.Builder()
                .add(new CustomJsonAdapterFactory())
                .add(new RuntimeTypeJsonAdapterFactory())
                .build();
    }

    @Provides @Singleton
    MoshiConverterFactory provideMoshiConverterFactory(Moshi moshi) {
        return MoshiConverterFactory.create(moshi);
    }

    @Singleton @Provides
    SimpleXmlConverterFactory provideSimpleXmlConverterFactory() {
        return SimpleXmlConverterFactory.create();
    }

    @Singleton @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides @Singleton
    Cache getOkHttpCache(Context context) {
        return OkHttp3Downloader.createDefaultCache(context);
    }

    @Provides @Singleton
    OkHttpClient getOkHttpClient(Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(cache)
                .cookieJar(new JavaNetCookieJar(CookieHandler.getDefault()));
//                .addInterceptor(new EncodingInterceptor())
                //BELOW LINE IS FOR LOGGING IF YOU'RE DEBUGGING RETROFIT CALLS
//                .addNetworkInterceptor(new HttpLoggingInterceptor()
//                        .setLevel(HttpLoggingInterceptor.Level.BODY));

        //TODO: Remove if not supporting pre-Lollipop
        // Pre-lollipop we might have to work around TLS sockets not supporting TLS 1.2 by default
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            try {
//                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//                trustManagerFactory.init((KeyStore)null);
//                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//                    throw new IllegalStateException("Unexpected default trust managers: " + Arrays.toString(trustManagers));
//                }
//                builder.sslSocketFactory(new TLSSocketFactory(), (X509TrustManager)trustManagers[0]);
//            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IllegalStateException e) {
//                Crashlytics.logException(e);
//                Log.w("AppModule", "Error adding TLSSocketFactory: " + e.getMessage(), e);
//            }
//        }
        return builder.build();
    }

    @Provides @Singleton
    Picasso providePicasso(Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }



//    @Singleton @Provides /*@Named("notes")*/
//    WebService provideWebservice(OkHttpClient okHttpClient, SimpleXmlConverterFactory simpleXmlConverterFactory) {
//        return new Retrofit.Builder()
//                .baseUrl(WebService.NOTE_BASE_URL)
//                .addConverterFactory(simpleXmlConverterFactory)
//                .client(okHttpClient)
//                .build()
//                .create(WebService.class);
//    }


    @Singleton @Provides
    Executor provideHttpExecutor() {
        return Executors.newCachedThreadPool();
    }

    //TODO: Remove if not using Firebase
//    @Singleton @Provides
//    FirebaseAnalytics provideFirebaseAnalytics(Context context) {
//        return FirebaseAnalytics.getInstance(context);
//    }
}
