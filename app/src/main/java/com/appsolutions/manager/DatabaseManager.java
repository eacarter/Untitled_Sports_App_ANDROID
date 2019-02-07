package com.appsolutions.manager;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public class DatabaseManager {

    private FirebaseFirestore database;

    @Inject
    public DatabaseManager(Context context ){
        database = FirebaseFirestore.getInstance();
    }


    public void initializeUser(FirebaseUser firebaseUser){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("First Name", "");
        userInfo.put("Last Name", "");
        userInfo.put("Location", "");
        userInfo.put("Sports", Arrays.asList());
        userInfo.put("Friends", Arrays.asList());
        userInfo.put("Average Rating", 0);

        database.
                collection("Users").
                document(firebaseUser.getUid()).
                set(userInfo, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("User", "User Created");
                }
                else{
                    Log.d("User", "User Creation Failed");
                }
            }
        });
    }

    public void updateUser(FirebaseUser firebaseUser){
        DocumentReference user = database.collection("Users").document(firebaseUser.getUid());
    }

}
