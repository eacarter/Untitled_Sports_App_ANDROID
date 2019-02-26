package com.appsolutions.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentLoginBinding;
import com.appsolutions.register.RegisterFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class LoginFragment extends DaggerFragment{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    @Inject
    public LoginFragment() {
        //Required empty public constructor
    }

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(LoginViewModel.class);
        lifecycleOwner = this;

        binding.setViewModelLogin(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.loginSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.login(binding.loginEmail.getText().toString(),
                        binding.loginPass.getText().toString(),
                        getActivity());

                viewModel.getUser().observe(lifecycleOwner, firebaseUser -> {
                    if(firebaseUser != null){
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .remove(getActivity().getSupportFragmentManager().findFragmentByTag("Login"))
                                .commit();
                    }
                    else{
                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        binding.loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.register(binding.loginEmail.getText().toString(),
//                        binding.loginPass.getText().toString(),
//                        getActivity());
//                viewModel.getUser().observe(lifecycleOwner, firebaseUser -> {
//                    if(firebaseUser != null){
//                        Log.d("Login", "You've successfully logged in");
//                    }
//                    else{
//                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_container, new RegisterFragment(), "Register")
                        .addToBackStack("Register")
                        .commit();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }
}
