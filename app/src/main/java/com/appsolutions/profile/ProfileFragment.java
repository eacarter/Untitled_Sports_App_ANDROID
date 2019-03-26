package com.appsolutions.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentHoopBinding;
import com.appsolutions.databinding.FragmentProfileBinding;
import com.appsolutions.feed.FeedAdapter;
import com.appsolutions.hoop.SquadAdapter;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Feed;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private ProfileAdapter profileAdapter;
    private FriendsAdapter friendsAdapter;
    private SquadAdapter squadAdapter;
    private List<String> friends;

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

        viewModel.getCurrentUser().observe(this, currentUser -> {
            viewModel.getUserData(currentUser.getUid()).observe(this, user ->{
                Glide.with(getContext())
                        .load(user.getProfile_image())
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.profileImage);

                binding.profileName.setText(user.getUsername());
                binding.profileAboutText.setText(user.getAbout());

            });
        });

        binding.profileAboutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutEdit(userManager.getUser().getValue()).show();
            }
        });
        loadData();

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

        binding.profilePostList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.profileFriendsList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.profileSquadList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        profileAdapter = new ProfileAdapter(getContext(), databaseManager, userManager, getActivity().getSupportFragmentManager());
        friendsAdapter = new FriendsAdapter(getContext(), databaseManager, userManager, getActivity().getSupportFragmentManager());
        squadAdapter = new SquadAdapter(getContext(), databaseManager, userManager, getActivity().getSupportFragmentManager());

        binding.profilePostList.setAdapter(profileAdapter);
        binding.profileFriendsList.setAdapter(friendsAdapter);
        binding.profileSquadList.setAdapter(squadAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    public AlertDialog aboutEdit(FirebaseUser firebaseUser){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.dialog_feed, null);
        builder.setView(view);
        builder.setMessage("Describe Yourself");
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText text = (EditText) view.findViewById(R.id.message_post);
                viewModel.updateFieldItem(firebaseUser, "about", text.getText().toString());
                binding.profileAboutText.setText(text.getText().toString());
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    public void loadData() {

        List<Feed> newFeed = new ArrayList<>();

        if (userManager.getUser().getValue() == null) {

        } else {
            viewModel.getFeedItems(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, feed -> {
                if (feed != null) {
                    for(Feed item : feed){
                        if(item.getId().contains(userManager.getUser().getValue().getUid())){
                            newFeed.add(item);
                        }
                    }
                    profileAdapter.setItems(newFeed);
                }
            });

            viewModel.getFriendList(userManager.getUser().getValue().getUid()).observe(this, list -> {
                friends = list;
                viewModel.findUsers(friends, userManager.getUser().getValue().getUid()).observe(this, friend -> {
                    friendsAdapter.setItems(friend);
                });
            });

            viewModel.getSquad(userManager.getUser().getValue().getUid()).observe(this, squad -> {
                viewModel.findUsers(squad, userManager.getUser().getValue().getUid()).observe(this, squadList -> {
                    squadAdapter.setItems(squadList);
                });
            });
        }
    }
}
