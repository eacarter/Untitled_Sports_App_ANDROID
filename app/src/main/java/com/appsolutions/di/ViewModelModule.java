package com.appsolutions.di;

import com.appsolutions.MainViewModel;
import com.appsolutions.login.LoginViewModel;
import com.appsolutions.register.RegisterAdditionalViewModel;
import com.appsolutions.register.RegisterPhotoViewModel;
import com.appsolutions.register.RegisterViewModel;

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
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindRegisterViewModel(RegisterViewModel registerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterAdditionalViewModel.class)
    abstract ViewModel bindRegisterAdditionalViewModel(RegisterAdditionalViewModel registerAdditionalViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterPhotoViewModel.class)
    abstract ViewModel bindRegisterPhotoViewModel(RegisterPhotoViewModel registerPhotoViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
