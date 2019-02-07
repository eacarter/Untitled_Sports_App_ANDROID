package com.appsolutions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appsolutions.databinding.ActivityMainBinding;
import com.appsolutions.login.LoginFragment;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.facebook.CallbackManager;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
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

    private ActivityMainBinding binding;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        userManager.getUser().observe(this, user ->{

            if(user == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new LoginFragment(), "Login")
                        .commit();
            }
            else{
                getSupportFragmentManager().beginTransaction()
                        .remove(getSupportFragmentManager().findFragmentByTag("Login"))
                        .commit();
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

//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onFragmentInteraction(String id) {
//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setTitle("Product Id");
//        alertDialog.setMessage(id);
//        alertDialog.setCancelable(true);
//        alertDialog.show();
//    }
}
