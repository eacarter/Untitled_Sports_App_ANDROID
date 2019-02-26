package com.appsolutions.register;

import android.app.Activity;
import android.location.Location;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

import javax.inject.Inject;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class RegisterViewModel extends BaseViewModel {

    private UserManager userManager;
    private DatabaseManager databaseManager;
    private UserMediatorLiveData userMediatorLiveData;
    private LocationManager locationManager;

    @Inject
    public RegisterViewModel(UserManager userManager, DatabaseManager databaseManager, LocationManager locationManager){
        this.userManager = userManager;
        this.databaseManager = databaseManager;
        this.locationManager = locationManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public void setLocation(Activity activity){
        locationManager.getLastKnownLocation(activity);
    }

    public LiveData<Location> getLocation(){
        return locationManager.getLocation();
    }

    public void register(String email, String pass, Map<String, Object> map, Activity activity, FragmentManager fragmentManager){
        userManager.Register(email, pass, map, activity, fragmentManager);
    }

    public DocumentReference createUser(FirebaseUser user){
        return databaseManager.updateUser(user);
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
