package com.appsolutions.di;

import com.appsolutions.login.LoginFragment;
import com.appsolutions.register.RegisterAdditionalFragment;
import com.appsolutions.register.RegisterFragment;
import com.appsolutions.register.RegisterPhotoFragment;

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

    @ContributesAndroidInjector
    abstract RegisterFragment bindRegisterFragment();

    @ContributesAndroidInjector
    abstract RegisterAdditionalFragment bindRegisterAdditionalFragment();

    @ContributesAndroidInjector
    abstract RegisterPhotoFragment bindRegisterPhotoFragment();

}
