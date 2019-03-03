package com.appsolutions.profile;

import android.app.Activity;

import com.appsolutions.manager.UserManager;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ProfileViewModel extends BaseViewModel {

    private UserManager userManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public ProfileViewModel(UserManager userManager){
        this.userManager = userManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public void login(String email, String pass, Activity activity){
        userManager.SignIn(email, pass, activity);
    }

//    public void register(String email, String pass, Activity activity){
//        userManager.Register(email, pass, activity);
//    }

    public LiveData<FirebaseUser> getUser(){
        return userManager.getUser();
    }

    private class UserMediatorLiveData extends MediatorLiveData<FirebaseUser> {
        UserMediatorLiveData(LiveData<FirebaseUser> source){
            addSource(source, user -> {
                setValue(user);
            });
        }
    }
}
