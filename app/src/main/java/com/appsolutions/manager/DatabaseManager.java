package com.appsolutions.manager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.appsolutions.R;
import com.appsolutions.models.Feed;
import com.appsolutions.models.User;
import com.appsolutions.register.RegisterPhotoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DatabaseManager {

    private FirebaseFirestore database;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Context context;
    private DocumentSnapshot documentSnapshot;

    @Inject
    public DatabaseManager(Context context) {
        this.context = context;
        database = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }


    public void initializeUser(FirebaseAuth firebaseAuth, Map<String, Object> map, FragmentManager manager) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", "");
        userInfo.put("username", "");
        userInfo.put("firstname", "");
        userInfo.put("lastname", "");
        userInfo.put("city", "");
        userInfo.put("state", "");
        userInfo.put("zipcode", "");
        userInfo.put("height", "");
        userInfo.put("weight", "");
        userInfo.put("age", "");
        userInfo.put("about", "");
        userInfo.put("profile_image", "");
        userInfo.put("gender", "");
        userInfo.put("dominant_hand", "");
        userInfo.put("latitude", 0.0);
        userInfo.put("longitude", 0.0);
        userInfo.put("medic-info", false);
        userInfo.put("friends", Arrays.asList());
        userInfo.put("endorsements", Arrays.asList());
        userInfo.put("average_rating", 0);
        userInfo.put("squad", Arrays.asList());

        database.
                collection("Users").
                document(firebaseAuth.getCurrentUser().getUid()).
                set(userInfo, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    updateUser(firebaseAuth.getCurrentUser()).update(map);

                    database.collection("Users").document(firebaseAuth.getCurrentUser()
                            .getUid()).update("friends", FieldValue.arrayUnion(firebaseAuth.getCurrentUser().getUid()));

                    updateUser(firebaseAuth.getCurrentUser()).update("id", firebaseAuth.getCurrentUser().getUid());

                    manager.beginTransaction()
                            .replace(R.id.login_container, new RegisterPhotoFragment(), "RegisterPhoto")
                            .addToBackStack("RegisterPhoto")
                            .commit();
                    Log.d("User", "User Created");
                } else {
                    Log.d("User", "User Creation Failed");
                }
            }
        });


    }

    public DocumentReference updateUser(FirebaseUser firebaseUser) {
        DocumentReference user = database.collection("Users").document(firebaseUser.getUid());
        return user;
    }

    public void uploadFeedItem(String id, Feed item) {
        database.collection("Feed").document(id).set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("FeedItem", "Feed Item Uploaded");
                }
            }
        });
    }

    public LiveData<List<Feed>> getFeedItems(String id){
        MutableLiveData<List<Feed>> feed = new MutableLiveData<>();

        database.collection("Feed").orderBy("timeStamp", Query.Direction.DESCENDING).limit(20).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Feed> list = new ArrayList<>();
                    for(QueryDocumentSnapshot document: task.getResult()){
                        list.add(document.toObject(Feed.class));
                    }
                    feed.setValue(list);
                }
            }
        });
        return feed;
    }

    public void addComment(String id, String postId, Feed item){
        database.collection("Feed").document(id)
                .collection("Comment").document(postId)
                .set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("FeedItem", "Feed Item Uploaded");
                }
            }
        });
    }

    public LiveData<List<Feed>> getCommentItems(String id){
        MutableLiveData<List<Feed>> feed = new MutableLiveData<>();

        database.collection("Feed").document(id).collection("Comment")
                .orderBy("timeStamp", Query.Direction.ASCENDING).limit(20).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Feed> list = new ArrayList<>();
                    for(QueryDocumentSnapshot document: task.getResult()){
                        list.add(document.toObject(Feed.class));
                    }
                    feed.setValue(list);
                }
            }
        });
        return feed;
    }

    public void addFriend(FirebaseUser firebaseUser, String userID){

        database.collection("Users")
                .document(firebaseUser.getUid())
                .update("friends", FieldValue.arrayUnion(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Friends", "friend added");
                        }
                    }
                });
    }

    public void removeFriend(FirebaseUser firebaseUser, String userID){

        database.collection("Users")
                .document(firebaseUser.getUid())
                .update("friends", FieldValue.arrayRemove(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("Friends", "friend removed");
                        }
                    }
                });
    }

    public LiveData<List<String>> getFriends(FirebaseUser firebaseUser){
        MutableLiveData<List<String>> friendsList = new MutableLiveData<>();

        database.collection("Users")
                .document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        List<String> list = (List<String>) documentSnapshot.get("friends");
                        friendsList.setValue(list);
                    }
                }
            }
        });

        return friendsList;
    }

    public void findUser(String username){
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        Query query = database.collection("Users").whereEqualTo("id", username);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot documentSnapshot = task.getResult();
//                    List<User> usersList = documentSnapshot.getDocuments();


                }
            }
        });
    }

    public LiveData<List<User>> getUserItems(){
        MutableLiveData<List<User>> users = new MutableLiveData<>();

        database.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<User> list = new ArrayList<>();
                    for(QueryDocumentSnapshot document: task.getResult()){
                        list.add(document.toObject(User.class));
                    }
                    users.setValue(list);
                }
            }
        });
        return users;
    }


    public Task<Void> updateItem(FirebaseUser firebaseUser, String field, String data){
        return database.collection("Users").document(firebaseUser.getUid()).update(field, data);
    }

    public DocumentReference createGame(String id){
        return database.collection("Game").document(id);
    }

    public LiveData<User> getUser(String id){
        MutableLiveData<User> user = new MutableLiveData<>();

        database.collection("Users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    user.setValue(documentSnapshot.toObject(User.class));
                }
            }
        });

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
                                updateUser(user).update("profile_image", uri.toString());
                            }
                        });
                        Toast.makeText(context, "file Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
