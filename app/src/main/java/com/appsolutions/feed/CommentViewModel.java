package com.appsolutions.feed;

import android.app.Activity;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Feed;
import com.appsolutions.models.User;
import com.appsolutions.widget.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class CommentViewModel extends BaseViewModel {

    private UserManager userManager;
    private DatabaseManager databaseManager;
    private UserMediatorLiveData userMediatorLiveData;

    @Inject
    public CommentViewModel(UserManager userManager, DatabaseManager databaseManager){
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
//
//    public void uploadFeedItem(String id, Feed feed){
//        databaseManager.uploadFeedItem(id, feed);
//    }

    public LiveData<List<Feed>> getCommentItems(String id){
       return databaseManager.getCommentItems(id);
    }

    public void sendCommentItem(String id, String postId, Feed feed){
        databaseManager.addComment(id, postId, feed);
    }

    public LiveData<User> getUserDB(String id){
         return databaseManager.getUser(id);
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
