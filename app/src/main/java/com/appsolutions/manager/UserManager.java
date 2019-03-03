package com.appsolutions.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.appsolutions.R;
import com.appsolutions.register.RegisterPhotoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserManager {

    private Context context;
    private FirebaseAuth firebaseAuth;
    private DatabaseManager databaseManager;
    //    private FirebaseUser firebaseUser;
    private MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();
    private MutableLiveData<FirebaseAuth> firebaseAuthMute = new MutableLiveData<>();

    @Inject
    public UserManager(Context context, DatabaseManager databaseManager) {
        this.context = context;
        this.databaseManager = databaseManager;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public LiveData<FirebaseUser> getUser(){
        firebaseUser.setValue(firebaseAuth.getCurrentUser());
        return firebaseUser;
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

    public void SignIn(String email, String password, Activity activity){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.isComplete()) {
                            firebaseUser.setValue(firebaseAuth.getCurrentUser());

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Register(String email, String password, Map<String, Object> map, Activity activity, FragmentManager manager){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.isComplete()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            databaseManager.initializeUser(firebaseAuth, map, manager);
                            firebaseUser.setValue(firebaseAuth.getCurrentUser());

                        } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(activity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

