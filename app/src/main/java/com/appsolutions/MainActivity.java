package com.appsolutions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appsolutions.databinding.ActivityMainBinding;
import com.appsolutions.login.LoginFragment;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.register.RegisterFragment;
import com.appsolutions.register.RegisterPhotoFragment;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    LibApplication application;

    @Inject
    UserManager userManager;

    @Inject
    LocationManager locationManager;

    @Inject
    DatabaseManager databaseManager;

    @Inject
    Executor executor;

    public final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private Activity activity;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        activity = this;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, new RegisterFragment(), "Login")
                .commit();
//
//        userManager.getUser().observe(this, user -> {
//            if(user != null){
//                getSupportFragmentManager().beginTransaction()
//                        .remove(getSupportFragmentManager().findFragmentByTag("Login"))
//                        .commit();
//            }
//            else{
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.login_container, new RegisterFragment(), "Login")
//                        .commit();
//            }
//        });




        initOtherViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Don't create the menu for now
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(!checkPermissions()){
            requestPermissions();
        }
        else {
            locationManager.getLastKnownLocation(this);
        }
    }
    private void initOtherViews() {
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(MainViewModel.class);
        viewModel.resume();
        binding.setViewModelMain(viewModel);

        binding.bottomNav.setViewModel(viewModel);
    }

    private boolean checkPermissions(){
        int state = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return state == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.READ_EXTERNAL_STORAGE);


        if (!shouldProvideRationale) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("In order to take full advantage of this app, please accept all permissions.");
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    activity.finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
