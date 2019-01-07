package com.target.dealbrowserpoc.dealbrowser.di;

import com.target.dealbrowserpoc.dealbrowser.DealListFragment;

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

    @ContributesAndroidInjector
    abstract DealListFragment bindDealListFragment();

}
