package com.appsolutions.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentProfileThirdBinding;

import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class ProfileThirdFragment extends DaggerFragment{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    DatabaseManager databaseManager;

    @Inject
    UserManager userManager;

    private FragmentProfileThirdBinding binding;
    private ProfileThirdViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private String items;
    private String userId;
    private FirebaseAuth firebaseAuth;

    @Inject
    public ProfileThirdFragment() {
        //Required empty public constructor
    }

    public static ProfileThirdFragment getInstance(String item) {
        ProfileThirdFragment profileThirdFragment = new ProfileThirdFragment();
        Bundle args = new Bundle();
        args.putString("item",item);
        profileThirdFragment.setArguments(args);
        return profileThirdFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(ProfileThirdViewModel.class);
        lifecycleOwner = this;

        if(getArguments() != null) {
            userId = getArguments().getString("item");
        }



        viewModel.getUserData(userId).observe(this, user -> {
            Glide.with(getContext())
                    .load(user.getProfile_image())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImage);

            binding.profileName.setText(user.getUsername());
            binding.profileAboutText.setText(user.getAbout());
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getUser().observe(lifecycleOwner, currentUser ->{
                    viewModel.followUser(currentUser, userId);
                });
            }
        });

        binding.setViewModelProfileThird(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile_third, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

//    private void followerCheck(){
//        viewModel.getUser().observe(lifecycleOwner, user ->{
//            viewModel.getFollowers(user).observe(lifecycleOwner, );
//        });
//    }
}
