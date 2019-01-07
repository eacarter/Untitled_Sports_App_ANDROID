package com.target.dealbrowserpoc.dealbrowser.di;

import com.target.dealbrowserpoc.dealbrowser.MainActivity;

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
