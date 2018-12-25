package com.target.dealbrowserpoc.dealbrowser;

import android.content.Context;
import android.content.Intent;

import com.target.dealbrowserpoc.dealbrowser.di.AppComponent;
import com.target.dealbrowserpoc.dealbrowser.di.DaggerAppComponent;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.inject.Inject;

import androidx.multidex.MultiDex;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

//import com.jacapps.bobandtom.library.repo.AppConfigRepository;

public class LibApplication extends DaggerApplication {

    @Inject
    AppComponent appComponent;

    //TODO: Delete if not using
//    @Inject
//    AppConfigRepository appConfigRepository;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        appComponent.inject(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .create(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static AppComponent getAppComponent(Context context) {
        return ((LibApplication)context.getApplicationContext()).getAppComponent();
    }

    //TODO: Delete if not using
//    public AppConfigRepository getAppConfigRepository() {
//        return appConfigRepository;
//    }

//    @Override
//    public Intent getMediaCompanionIntent() {
//        return new Intent(this, AppMediaCompanionService.class);
//    }
}
