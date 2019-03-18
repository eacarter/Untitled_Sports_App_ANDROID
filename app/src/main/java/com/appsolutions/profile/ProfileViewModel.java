package com.appsolutions.profile;

import android.app.Activity;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.User;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ProfileViewModel extends BaseViewModel {

    private UserManager userManager;
    private DatabaseManager databaseManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public ProfileViewModel(UserManager userManager, DatabaseManager databaseManager){
        this.userManager = userManager;
        this.databaseManager = databaseManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userManager.getUser();
    }

    public LiveData<User> getUserData(String id){
        return databaseManager.getUser(id);
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
