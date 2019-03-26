package com.appsolutions.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationManager {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private MutableLiveData<Double> longitude;
    private MutableLiveData<Double> latitude;
    private MutableLiveData<Location> location;
    private PlacesClient placesClient;
    private Context context;
    private SharedPreferences sharedPreferences;

    @Inject
    public LocationManager(Context context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Places.initialize(context, "AIzaSyCGUK0WLvJ7Z4RANKG_gNYUnqlokxi8FTs" );
        placesClient = Places.createClient(context);
        this.context = context;
        longitude = new MutableLiveData<>();
        latitude = new MutableLiveData<>();
        location = new MutableLiveData<>();
    }


    public void getLastKnownLocation(Activity activity) {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(activity, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if (task.isSuccessful() && task.getResult() != null) {
                    longitude.setValue(task.getResult().getLatitude());
                    latitude.setValue(task.getResult().getLongitude());
                    location.setValue(task.getResult());

                    try {

                        Geocoder gcd = new Geocoder(activity, Locale.getDefault());
                        List<Address> addresses = gcd.getFromLocation(task.getResult().getLatitude(), task.getResult().getLongitude(), 1);

                        Log.d("Location", latitude + ", " + longitude);

                        if (addresses.size() > 0) {
                            SharedPreferences.Editor editor = context.getSharedPreferences("Location", Context.MODE_PRIVATE).edit();
                            editor.putString("city", addresses.get(0).getLocality());
                            editor.putString("state", addresses.get(0).getAdminArea());
                            editor.putString("zip", addresses.get(0).getPostalCode());
                            editor.putFloat("latitude", (float) task.getResult().getLatitude());
                            editor.putFloat("longitude", (float) task.getResult().getLongitude());
                            editor.commit();
                        }
                    } catch (IOException e) {
                        Log.d("Registration", e.getMessage());
                    }
                } else {
                    Log.d("Task", task.getException().getMessage());
                }
            }


        });
    }

    public LiveData<Location> getLocation(){
        return location;
    }

    public LiveData<List<PlaceLikelihood>> getLocalPlaces(){
        MutableLiveData<List<PlaceLikelihood>> placeLivedata = new MutableLiveData<>();

        List<Place.Field> places = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES);

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(places).build();

        Task<FindCurrentPlaceResponse> task = placesClient.findCurrentPlace(request);

        task.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                List<PlaceLikelihood> placesList = new ArrayList<>();

                if(task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    for(PlaceLikelihood placeLikelihood: response.getPlaceLikelihoods()){
                        if(placeLikelihood.getPlace().getTypes().contains("gym") ||
                                placeLikelihood.getPlace().getTypes().contains("park")){
                            placesList.add(placeLikelihood);
                        }
                    }
                    placeLivedata.setValue(placesList);
                }
                else{
                    Log.d("places", task.getException().getMessage());
                }
            }
        });

        return placeLivedata;
    }
}
