package com.appsolutions.di;

import com.appsolutions.login.LoginFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Module to support injecting activities.
 *
 * Bind all activity classes here.
 */
@Module
abstract class FragmentBuilderModule {
//    @ContributesAndroidInjector
//    abstract MainActivityFragment bindMainActivityFragment();

//    @ContributesAndroidInjector
//    abstract DealListFragment bindDealListFragment();

    @ContributesAndroidInjector
    abstract LoginFragment bindLoginFragment();

}
