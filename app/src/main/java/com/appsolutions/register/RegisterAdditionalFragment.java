package com.appsolutions.register;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentRegisterAdditionalBinding;
import com.appsolutions.manager.LocationManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class RegisterAdditionalFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    LocationManager locationManager;

    private FragmentRegisterAdditionalBinding binding;
    private RegisterAdditionalViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private double latitude;
    private double longitude;

    @Inject
    public RegisterAdditionalFragment() {
        //Required empty public constructor
    }

    public static RegisterAdditionalFragment getInstance() {
        return new RegisterAdditionalFragment();
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
                viewModelFactory).get(RegisterAdditionalViewModel.class);
        lifecycleOwner = this;

        binding.setViewModelRegisterAdditional(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_register_additional, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.registerNextAdditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                viewModel.getUser().observe(lifecycleOwner, firebaseUser -> {
//                    if(firebaseUser != null) {
//                        if (binding.registerHeight.getText().toString().isEmpty() ||
//                                binding.registerWeight.getText().toString().isEmpty() ||
//                                binding.registerAge.getText().toString().isEmpty()) {
//                            errorDialog(getActivity());
//                        } else {
//                            Map<String, Object> userInitInfo = new HashMap<>();
//                            userInitInfo.put("height", binding.registerHeight.getText().toString());
//                            userInitInfo.put("weight", binding.registerWeight.getText().toString());
//                            userInitInfo.put("age", binding.registerAge.getText().toString());
//                            userInitInfo.put("gender", binding.registerGender.getSelectedItem());
//                            userInitInfo.put("dominant-hand", binding.registerHand.getSelectedItem());
//                            viewModel.createUser(firebaseUser).update(userInitInfo);
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.login_container, new RegisterAdditionalFragment(), "RegisterAdditional")
//                                    .addToBackStack("RegisterAdditional")
//                                    .commit();
//                        }
//                    }
//            });
        }
        });

        return binding.getRoot();
    }

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
}
