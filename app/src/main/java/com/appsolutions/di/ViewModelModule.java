package com.appsolutions.di;

import com.appsolutions.MainViewModel;
import com.appsolutions.feed.CommentViewModel;
import com.appsolutions.feed.FeedViewModel;
import com.appsolutions.hoop.CreateGameViewModel;
import com.appsolutions.hoop.HoopViewModel;
import com.appsolutions.login.LoginViewModel;
import com.appsolutions.notification.NotifViewModel;
import com.appsolutions.profile.ProfileThirdViewModel;
import com.appsolutions.profile.ProfileViewModel;
import com.appsolutions.register.RegisterAdditionalViewModel;
import com.appsolutions.register.RegisterPhotoViewModel;
import com.appsolutions.register.RegisterViewModel;
import com.appsolutions.setting.SettingsViewModel;

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
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    abstract ViewModel bindSettingsViewModel(SettingsViewModel settingsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotifViewModel.class)
    abstract ViewModel bindNotifViewModel(NotifViewModel notifViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel.class)
    abstract ViewModel bindFeedViewModel(FeedViewModel feedViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HoopViewModel.class)
    abstract ViewModel bindHoopViewModel(HoopViewModel hoopViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CreateGameViewModel.class)
    abstract ViewModel bindCreateGameViewModel(CreateGameViewModel createGameViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CommentViewModel.class)
    abstract ViewModel bindCommentViewModel(CommentViewModel commentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileThirdViewModel.class)
    abstract ViewModel bindProfileThirdViewModel(ProfileThirdViewModel profileThirdViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
