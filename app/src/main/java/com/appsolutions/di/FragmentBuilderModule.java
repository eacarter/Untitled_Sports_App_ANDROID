package com.appsolutions.di;

import com.appsolutions.feed.CommentFragment;
import com.appsolutions.feed.FeedFragment;
import com.appsolutions.hoop.CreateGameFragment;
import com.appsolutions.hoop.CreateStepOne;
import com.appsolutions.hoop.CreateStepThree;
import com.appsolutions.hoop.CreateStepTwo;
import com.appsolutions.hoop.HoopFragment;
import com.appsolutions.login.LoginFragment;
import com.appsolutions.notification.NotifFragment;
import com.appsolutions.profile.ProfileFragment;
import com.appsolutions.register.RegisterAdditionalFragment;
import com.appsolutions.register.RegisterFragment;
import com.appsolutions.register.RegisterPhotoFragment;
import com.appsolutions.setting.SettingsFragment;

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

    @ContributesAndroidInjector
    abstract SettingsFragment bindSettingsFragment();

    @ContributesAndroidInjector
    abstract NotifFragment bindNotifFragment();

    @ContributesAndroidInjector
    abstract FeedFragment bindFeedFragment();

    @ContributesAndroidInjector
    abstract HoopFragment bindHoopFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment bindProfileFragment();

    @ContributesAndroidInjector
    abstract CreateGameFragment bindCreateGameFragment();

    @ContributesAndroidInjector
    abstract CreateStepOne bindCreateStepOne();

    @ContributesAndroidInjector
    abstract CreateStepTwo bindCreateStepTwo();

    @ContributesAndroidInjector
    abstract CreateStepThree bindCreateStepThree();

    @ContributesAndroidInjector
    abstract CommentFragment bindCommentFragment();

}
