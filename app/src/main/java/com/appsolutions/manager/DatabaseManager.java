package com.appsolutions.manager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public class DatabaseManager {

    private FirebaseFirestore database;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Context context;

    @Inject
    public DatabaseManager(Context context ){
        this.context = context;
        database = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }


    public void initializeUser(FirebaseUser firebaseUser){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("First Name", "");
        userInfo.put("Last Name", "");
        userInfo.put("city", "");
        userInfo.put("state", "");
        userInfo.put("zipcode", "");
        userInfo.put("height", "");
        userInfo.put("weight", "");
        userInfo.put("age", "");
        userInfo.put("profile_image", "");
        userInfo.put("gender", "");
        userInfo.put("dominant-hand", "");
        userInfo.put("latitude", 0.0);
        userInfo.put("longitude", 0.0);
        userInfo.put("medic-info", false);
        userInfo.put("endorsements", Arrays.asList());
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

    public DocumentReference updateUser(FirebaseUser firebaseUser){
        DocumentReference user = database.collection("Users").document(firebaseUser.getUid());
        return user;
    }

    public void uploadImage(Uri file, FirebaseUser user){
        if(file != null){
            StorageReference storage = storageReference.child("profile_images/" + user.getUid().toString());
            storage.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        StorageReference storage = storageReference.child("profile_images/" + user.getUid().toString());
                        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateUser(user).update("image", uri.toString());
                            }
                        });
                        Toast.makeText(context, "file Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
