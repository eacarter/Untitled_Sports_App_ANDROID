package com.appsolutions.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentSettingsBinding;
import com.appsolutions.login.LoginFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class SettingsFragment extends DaggerFragment implements View.OnClickListener{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    @Inject
    public SettingsFragment() {
        //Required empty public constructor
    }

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(SettingsViewModel.class);
        lifecycleOwner = this;

        binding.setViewModelLogin(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_settings, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.settingsLogout.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.settingsLogout.getId()){
            viewModel.signOut();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_container, new LoginFragment(), "Login")
                    .commit();
        }
    }
}
