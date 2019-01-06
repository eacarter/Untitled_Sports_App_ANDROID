package com.appsolutions.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentLoginBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class LoginFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

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

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }
}
