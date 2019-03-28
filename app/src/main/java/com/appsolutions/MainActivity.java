package com.appsolutions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.appsolutions.adapters.UserAdapter;
import com.appsolutions.databinding.ActivityMainBinding;
import com.appsolutions.feed.CommentFragment;
import com.appsolutions.feed.FeedFragment;
import com.appsolutions.hoop.HoopFragment;
import com.appsolutions.login.LoginFragment;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.User;
import com.appsolutions.notification.NotifFragment;
import com.appsolutions.profile.ProfileFragment;
import com.appsolutions.setting.SettingsFragment;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.DaggerFragment;

public class MainActivity extends DaggerAppCompatActivity implements View.OnClickListener {

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
    private MapEngine mapEngine;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        activity = this;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, new LoginFragment(), "Login")
                .commit();
//
//
        userManager.getUser().observe(this, user -> {
            if(user != null){
//                if(getSupportFragmentManager().findFragmentByTag("login").isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag("Login"))
                            .commit();
//                }
            }
            else{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_container, new LoginFragment(), "Login")
                        .commit();
            }
        });

        binding.bottomNav.menuHome.setSelected(true);
        binding.bottomNav.menuHome.setOnClickListener(this);
        binding.bottomNav.menu27.setOnClickListener(this);
        binding.bottomNav.menuAudio.setOnClickListener(this);
        binding.bottomNav.menuVideo.setOnClickListener(this);
        binding.bottomNav.menuMore.setOnClickListener(this);


        initOtherViews();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FeedFragment(), "Feed")
                .commit();

        mapEngine = MapEngine.getInstance();
        mapEngine.init(getApplicationContext(), new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(Error error) {
                if(error == Error.NONE){

                }
                else{
                    Log.d("mapEngine", "Failed to initialize");
                }
            }
        });
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

    private void showContent(DaggerFragment fragment, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .commit();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.bottomNav.menuHome.getId()){
//            bottomSelector(binding.bottomNav.menuHome.getId());
//            layout = findViewById(R.id.top_play);
//            layout.setVisibility(View.INVISIBLE);
            showContent(FeedFragment.getInstance(), "Feed");
        }
        else if(v.getId() == binding.bottomNav.menu27.getId()){
//            bottomSelector(binding.bottomNav.menu27.getId());
//            layout = findViewById(R.id.top_play);
//            layout.setVisibility(View.INVISIBLE);
            showContent(HoopFragment.getInstance(), "Listen");
        }
        else if(v.getId() == binding.bottomNav.menuAudio.getId()){
//            bottomSelector(binding.bottomNav.menuAudio.getId());
//            layout = findViewById(R.id.top_play);
//            layout.setVisibility(View.INVISIBLE);
            showContent(ProfileFragment.getInstance(), "Audio");
        }
        else if(v.getId() == binding.bottomNav.menuVideo.getId()){
//            bottomSelector(binding.bottomNav.menuVideo.getId());
//            layout = findViewById(R.id.top_play);
//            layout.setVisibility(View.INVISIBLE);
            showContent(NotifFragment.getInstance(), "Notifications");
        }
        else if(v.getId() == binding.bottomNav.menuMore.getId()){
//            bottomSelector(binding.bottomNav.menuMore.getId());
//            layout = findViewById(R.id.top_play);
//            layout.setVisibility(View.INVISIBLE);
            showContent(SettingsFragment.getInstance(), "Settings");
        }
    }
}
