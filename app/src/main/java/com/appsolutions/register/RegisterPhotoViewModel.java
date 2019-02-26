package com.appsolutions.register;

import android.net.Uri;

import com.appsolutions.login.LoginViewModel;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterPhotoViewModel extends BaseViewModel {

    private DatabaseManager databaseManager;
    private UserManager userManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public RegisterPhotoViewModel(UserManager userManager, DatabaseManager databaseManager, LocationManager locationManager){
        this.databaseManager = databaseManager;
        this.userManager = userManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public LiveData<FirebaseUser> getUser(){
        return userManager.getUser();
    }

    public void uploadPhoto(Uri uri, FirebaseUser user){
        databaseManager.uploadImage(uri, user);
    }

    private class UserMediatorLiveData extends MediatorLiveData<FirebaseUser> {
        UserMediatorLiveData(LiveData<FirebaseUser> source){
            addSource(source, user -> {
                setValue(user);
            });
        }
    }
}
