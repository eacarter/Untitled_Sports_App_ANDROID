package com.appsolutions.di;

//import com.jacapps.bobandtom.library.alarm.AlarmReceiver;
//import com.jacapps.bobandtom.library.alarm.RebootReceiver;
//import com.jacapps.bobandtom.library.widget.AlarmBindingAdapter;

import com.appsolutions.LibApplication;

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
//    void inject(AlarmBindingAdapter alarmBindingAdapter);
//    void inject(AlarmReceiver alarmReceiver);
//    void inject(RebootReceiver rebootReceiver);
}
