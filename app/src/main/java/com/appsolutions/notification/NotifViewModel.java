package com.appsolutions.notification;

import android.app.Activity;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Notifications;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class NotifViewModel extends BaseViewModel {

    private UserManager userManager;
    private UserMediatorLiveData userMediatorLiveData;
    private DatabaseManager databaseManager;

    @Inject
    public NotifViewModel(UserManager userManager, DatabaseManager databaseManager){
        this.userManager = userManager;
        this.databaseManager = databaseManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

//    public void login(String email, String pass, Activity activity){
//        userManager.SignIn(email, pass, activity);
//    }

    public LiveData<List<Notifications>> getNotifications(String id){
        return databaseManager.getNotifications(id);
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
