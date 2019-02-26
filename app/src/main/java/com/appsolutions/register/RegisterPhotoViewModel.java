package com.appsolutions.register;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.widget.BaseViewModel;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

public class RegisterPhotoViewModel extends BaseViewModel {

    @Inject
    public RegisterPhotoViewModel(UserManager userManager, DatabaseManager databaseManager, LocationManager locationManager){

    }

    @Override
    public void resume() {

    }
}
