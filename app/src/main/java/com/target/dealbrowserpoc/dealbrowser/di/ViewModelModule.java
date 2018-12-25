package com.target.dealbrowserpoc.dealbrowser.di;

import com.target.dealbrowserpoc.dealbrowser.DealItemViewModel;
import com.target.dealbrowserpoc.dealbrowser.DealListViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Module to support injecting view models.
 *
 * Bind all ViewModel classes here.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DealListViewModel.class)
    abstract ViewModel bindDealListViewModel(DealListViewModel dealListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DealItemViewModel.class)
    abstract ViewModel bindDealItemViewModel(DealItemViewModel dealItemViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
