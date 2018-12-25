package com.target.dealbrowserpoc.dealbrowser.di;

import com.target.dealbrowserpoc.dealbrowser.LibApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class,
        ActivityBuilderModule.class})
public interface AppComponent extends AndroidInjector<LibApplication> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<LibApplication> {
        public abstract AppComponent build();
    }

    void inject(LibApplication libApplication);
}
