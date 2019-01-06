package com.appsolutions.di;

import com.appsolutions.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Module to support injecting activities.
 *
 * Bind all activity classes here.
 */
@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract MainActivity bindMainActivity();


//    @ContributesAndroidInjector(modules = MyActivityModule.class) // leave out modules if not needed
//    abstract MyActivity bindMyActivity();
}
