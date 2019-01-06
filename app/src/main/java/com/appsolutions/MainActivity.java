package com.appsolutions;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appsolutions.databinding.ActivityMainBinding;
import com.appsolutions.login.LoginFragment;

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
    Executor executor;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
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

//    @Override
//    public void onFragmentInteraction(String id) {
//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setTitle("Product Id");
//        alertDialog.setMessage(id);
//        alertDialog.setCancelable(true);
//        alertDialog.show();
//    }
}
