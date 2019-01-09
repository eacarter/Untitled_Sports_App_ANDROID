package com.appsolutions.manager;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class LocationManager {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MutableLiveData<Double> longitude;
    private MutableLiveData<Double> latitude;
    private MutableLiveData<Location> location;
    private Context context;

    @Inject
    public LocationManager(Context context){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.context = context;
        longitude = new MutableLiveData<>();
        latitude = new MutableLiveData<>();
        location = new MutableLiveData<>();
    }

    @SuppressWarnings("MissingPermission")
    public void getLastKnownLocation(Activity activity) {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(activity, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if(task.isSuccessful() && task.getResult() != null) {
                    longitude.setValue(task.getResult().getLatitude());
                    latitude.setValue(task.getResult().getLongitude());
                    location.setValue(task.getResult());
                }
            }
        });
    }
}
