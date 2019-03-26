package com.appsolutions.profile;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Feed;
import com.appsolutions.models.Notifications;
import com.appsolutions.models.User;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ProfileThirdViewModel extends BaseViewModel {

    private UserManager userManager;
    private DatabaseManager databaseManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public ProfileThirdViewModel(UserManager userManager, DatabaseManager databaseManager){
        this.userManager = userManager;
        this.databaseManager = databaseManager;
        userMediatorLiveData = new UserMediatorLiveData(userManager.getUser());
    }

    @Override
    public void resume() {

    }

    public LiveData<User> getUserData(String id){
        return databaseManager.getUser(id);
    }

    public void followUser(FirebaseUser firebaseUser, String id){
        databaseManager.addFriend(firebaseUser, id);
    }

    public void unfollowUser(FirebaseUser firebaseUser, String id){
        databaseManager.removeFriend(firebaseUser, id);
    }

    public LiveData<List<String>> getFollowers(String id){
        return databaseManager.getFriends(id);
    }

    public LiveData<List<Feed>> getFeedItems(String id){
        return databaseManager.getFeedItems(id);
    }

    public LiveData<List<String>> getSquad(String id){
        return databaseManager.getSquad(id);
    }

    public LiveData<List<String>> getFriendList(String id){
        return databaseManager.getFriends(id);
    }

    public LiveData<List<User>>findUsers(List<String> users, String id){
        return databaseManager.findUsers(users, id);
    }

    public void addNotification(String id, String notifId, Notifications notifications){
        databaseManager.addNotification(id, notifId, notifications);
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
