package com.appsolutions.hoop;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Notifications;
import com.appsolutions.models.User;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class SquadViewModel extends BaseViewModel {

    private UserManager userManager;
    private DatabaseManager databaseManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public SquadViewModel(UserManager userManager, DatabaseManager databaseManager){
        this.userManager = userManager;
        this.databaseManager = databaseManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public void createGame(String id, Map<String, Object> maps){
         databaseManager.createGame(id, maps);
    }

    public LiveData<List<String>> getFriends(String id){
        return databaseManager.getFriends(id);
    }

    public LiveData<List<String>> getSquad(String id){
        return databaseManager.getSquad(id);
    }

    public void add2Squad(FirebaseUser firebaseUser, String id){
        databaseManager.addSquad(firebaseUser, id);
    }

    public void removeFromSquad(FirebaseUser firebaseUser, String id){
        databaseManager.removeSquad(firebaseUser, id);
    }

    public LiveData<List<User>>findUsers(List<String> users, String id){
        return databaseManager.findUsers(users, id);
    }
//    public void login(String email, String pass, Activity activity){
//        userManager.SignIn(email, pass, activity);
//    }

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
