package com.appsolutions.di;

import com.appsolutions.login.LoginViewModel;

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
    //@Binds
    //@IntoMap
    //@ViewModelKey(MyViewModel.class)
    //abstract ViewModel bindMyViewModel(MyViewModel myViewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(DealListViewModel.class)
//    abstract ViewModel bindMainViewModel(DealListViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
