package com.appsolutions.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentRegisterBinding;
import com.appsolutions.manager.LocationManager;
import com.appsolutions.manager.UserManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class RegisterFragment extends DaggerFragment{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    LocationManager locationManager;

    @Inject
    UserManager userManager;

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private double latitude;
    private double longitude;

    @Inject
    public RegisterFragment() {
        //Required empty public constructor
    }

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        locationManager.getLastKnownLocation(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(RegisterViewModel.class);
        lifecycleOwner = this;

        binding.setViewModelRegister(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_register, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Location", Context.MODE_PRIVATE);

        binding.registerCity.setText(sharedPreferences.getString("city", ""));
        binding.registerState.setText(sharedPreferences.getString("state", ""));
        binding.registerZip.setText(sharedPreferences.getString("zip", ""));
        latitude = sharedPreferences.getFloat("latitude", 0.0f);
        longitude = sharedPreferences.getFloat("longitude", 0.0f);

        binding.registerAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(getContext(), datePickerListener, mYear, mMonth, mDay);
                dateDialog.getDatePicker().setMaxDate(new Date().getTime());
                dateDialog.show();
            }
        });

        binding.registerHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                adapter.addAll(makeHeightFeet());

                AlertDialog.Builder height = new AlertDialog.Builder(getContext());
                height.setTitle("Enter your height: ");
                height.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.registerHeight.setText(adapter.getItem(which));
                    }
                });

                height.create().show();
            }
        });

        binding.registerWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                adapter.addAll(makeWeight());

                AlertDialog.Builder height = new AlertDialog.Builder(getContext());
                height.setTitle("Enter your weight: ");
                height.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.registerWeight.setText(adapter.getItem(which));
                    }
                });

                height.create().show();
            }
        });

        ArrayAdapter<String> adaptergender = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, gender());
        binding.registerGender.setAdapter(adaptergender);

        ArrayAdapter<String> adapterdominant = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, domHand());
        binding.registerHand.setAdapter(adapterdominant);

        binding.loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        if(binding.registerFirstName.getText().toString().isEmpty() ||
                                binding.registerLastName.getText().toString().isEmpty() ||
                                binding.registerCity.getText().toString().isEmpty() ||
                                binding.registerState.getText().toString().isEmpty()||
                                binding.registerZip.getText().toString().isEmpty()){
                            errorDialog(getActivity());
                        }
                        else {
                            Map<String, Object> userInitInfo = new HashMap<>();
                            userInitInfo.put("username", binding.registerEmail.getText().toString().split("@")[0]);
                            userInitInfo.put("firstname", binding.registerFirstName.getText().toString());
                            userInitInfo.put("lastname", binding.registerLastName.getText().toString());
                            userInitInfo.put("city", binding.registerCity.getText().toString());
                            userInitInfo.put("state", binding.registerState.getText().toString());
                            userInitInfo.put("zipcode", binding.registerZip.getText().toString());
                            userInitInfo.put("age", binding.registerAge.getText().toString());
                            userInitInfo.put("gender", binding.registerGender.getSelectedItem().toString());
                            userInitInfo.put("height", binding.registerHeight.getText().toString());
                            userInitInfo.put("weight", binding.registerWeight.getText().toString());
                            userInitInfo.put("dominant_hand", binding.registerHand.getSelectedItem().toString());
                            userInitInfo.put("latitude", latitude);
                            userInitInfo.put("longitude", longitude);

                            viewModel.register(binding.registerEmail.getText().toString(),
                                    binding.registerPass.getText().toString(), userInitInfo,
                                    getActivity(), getActivity().getSupportFragmentManager());

                        }
//                    }
//                    else{
//                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        return binding.getRoot();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            String format = new SimpleDateFormat("dd MMM YYYY").format(c.getTime());
            binding.registerAge.setText(Integer.toString(calculateAge(c.getTimeInMillis())));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    private AlertDialog errorDialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Fields May be missing data, Please double check.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }
        return age;
    }

    private ArrayList<String> makeWeight(){
        ArrayList<String> weight = new ArrayList<>();

        for(int i = 60; i <= 500; i++){
            weight.add(String.valueOf(i));
        }

        return weight;
    }

    private ArrayList<String> makeHeightFeet(){
        ArrayList<String> height = new ArrayList<>();

        for(int i = 3; i <= 8; i++){
            for(int j = 0; j <= 11; j++) {
                height.add(String.valueOf(i+"\'"+j));
            }
        }

        return height;
    }

    private String [] gender(){
        return new String[] {"Select Gender", "Non-Binary", "Female", "Male"};
    }

    private String [] domHand(){
        return new String[] {"Select Dominant Hand", "Left", "Right"};
    }
}
