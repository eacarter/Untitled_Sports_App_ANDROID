package com.target.dealbrowserpoc.dealbrowser.di;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;
import com.target.dealbrowserpoc.dealbrowser.LibApplication;
import com.target.dealbrowserpoc.dealbrowser.network.WebService;
import com.target.dealbrowserpoc.dealbrowser.util.EncodingInterceptor;
import com.target.dealbrowserpoc.dealbrowser.util.RuntimeTypeJsonAdapterFactory;

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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

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
//                //BELOW LINE IS FOR LOGGING IF YOU'RE DEBUGGING RETROFIT CALLS
//                .addNetworkInterceptor(new HttpLoggingInterceptor()
//                        .setLevel(HttpLoggingInterceptor.Level.BODY));

        return builder.build();
    }

    @Provides @Singleton
    Picasso providePicasso(Context context, OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }



    @Singleton @Provides
    WebService provideWebservice(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(WebService.ITEMS_URL)
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
                .create(WebService.class);
    }


    @Singleton @Provides
    Executor provideHttpExecutor() {
        return Executors.newCachedThreadPool();
    }

}
