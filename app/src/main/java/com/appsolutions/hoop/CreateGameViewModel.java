package com.appsolutions.hoop;

import android.app.Activity;

import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.widget.BaseViewModel;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class CreateGameViewModel extends BaseViewModel {

    private UserManager userManager;
    private LocationManager locationManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public CreateGameViewModel(UserManager userManager, LocationManager locationManager){
        this.userManager = userManager;
        this.locationManager = locationManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public void login(String email, String pass, Activity activity){
        userManager.SignIn(email, pass, activity);
    }

    public LiveData<List<PlaceLikelihood>> getPlaces(){
        return locationManager.getLocalPlaces();
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
