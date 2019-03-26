package com.appsolutions.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentProfileThirdBinding;

import com.appsolutions.hoop.SquadAdapter;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Feed;
import com.appsolutions.models.Notifications;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private ProfileAdapter profileAdapter;
    private FriendsAdapter friendsAdapter;
    private SquadAdapter squadAdapter;
    private List<String> friends;
    private FirebaseAuth firebaseAuth;
    private boolean follow;

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

        viewModel.getFollowers(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, friendsList ->{
            for(String user : friendsList){
                if(user.contains(userId)){
                    follow = true;
                    binding.fabText.setText("unfollow");
                    break;
                }
                else{
                    follow = false;
                    binding.fabText.setText("follow");
                }
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getUser().observe(lifecycleOwner, currentUser ->{
                    if(follow){
                        viewModel.unfollowUser(currentUser, userId);

                        viewModel.getUserData(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, user ->{
                            Notifications notify = new Notifications();
                            notify.setId(userId+""+randomGenerator());
                            notify.setImage(user.getProfile_image());
                            notify.setMessage(user.getUsername()+" has started following you, if " +
                                    "you follow back you can add them to your squad.");
                            notify.setTimeStamp(new Date().getTime());
                            notify.setType("follow");
                            viewModel.addNotification(userId, notify.getId(), notify);
                        });
                        binding.fabText.setText("follow");
                    }
                    else{
                        viewModel.followUser(currentUser, userId);
                        binding.fabText.setText("unfollow");
                    }
                });
            }
        });

        loadData();

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

    public void loadData() {

        List<Feed> newFeed = new ArrayList<>();

        if (userManager.getUser().getValue() == null) {

        } else {
            viewModel.getFeedItems(userId).observe(lifecycleOwner, feed -> {
                if (feed != null) {

                    for(Feed item : feed){
                        if(item.getId().contains(userId)){
                            newFeed.add(item);
                        }
                    }
                    profileAdapter.setItems(newFeed);
                }
            });

            viewModel.getFriendList(userId).observe(this, list -> {
                friends = list;

                viewModel.findUsers(friends, userId).observe(this, friend -> {
                    friendsAdapter.setItems(friend);
                });
            });

            viewModel.getSquad(userId).observe(this, squad -> {
                viewModel.findUsers(squad, userId).observe(this, squadList -> {
                    squadAdapter.setItems(squadList);
                });
            });
        }
    }

    private String randomGenerator(){
        String[] latter = {"a","b","c","d","e","f","g","h","i","j","k","l","m",
                "n","o","p","q","r","s","t","u","v","w","x","y","z","1","2","3","4","5","6","7"
                ,"8","9","0"};

        StringBuilder cat = new StringBuilder();

        for(int i = 0; i < 20; i++){
            cat.append(latter[new Random().nextInt(latter.length)]);
        }
        return cat.toString();
    }

//    private void followerCheck(){
//        viewModel.getUser().observe(lifecycleOwner, user ->{
//            viewModel.getFollowers(user).observe(lifecycleOwner, );
//        });
//    }
}
