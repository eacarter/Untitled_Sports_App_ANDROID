package com.appsolutions.login;

import android.app.Activity;
import android.util.Log;

import com.appsolutions.R;
import com.appsolutions.manager.UserManager;
import com.appsolutions.widget.BaseViewModel;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class LoginViewModel extends BaseViewModel {

    private UserManager userManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public LoginViewModel(UserManager userManager){
        this.userManager = userManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public void login(String email, String pass, Activity activity){
        userManager.SignIn(email, pass, activity);
    }

    public void register(String email, String pass, Activity activity){
        userManager.Register(email, pass, activity);
    }

    public void facebookLogin(AccessToken accessToken, Activity activity){
        userManager.handleFacebookAccessToken(accessToken, activity);
    }

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
