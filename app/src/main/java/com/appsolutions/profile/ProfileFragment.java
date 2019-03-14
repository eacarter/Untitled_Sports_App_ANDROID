package com.appsolutions.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentHoopBinding;
import com.appsolutions.databinding.FragmentProfileBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class ProfileFragment extends DaggerFragment{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    DatabaseManager databaseManager;

    @Inject
    UserManager userManager;

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private FirebaseAuth firebaseAuth;

    @Inject
    public ProfileFragment() {
        //Required empty public constructor
    }

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(ProfileViewModel.class);
        lifecycleOwner = this;

        binding.setViewModelProfile(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_profile, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

//        viewModel.getUser().observe(this, user ->{



        databaseManager.getUser(userManager.getUser().getValue().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(getContext())
                        .load(documentSnapshot.get("image"))
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImage);

                binding.profileName.setText(userManager.getUser().getValue().getEmail().split("@")[0]);
//                binding.profileAboutText.setText(documentSnapshot.get("about").toString());
            }
        });

//        });





        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }
}
