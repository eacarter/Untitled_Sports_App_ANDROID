package com.appsolutions.hoop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentHoopBinding;
import com.appsolutions.databinding.FragmentSquadBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.User;
import com.appsolutions.profile.FriendsAdapter;
import com.appsolutions.profile.ProfileAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class SquadFragment extends DaggerFragment implements View.OnClickListener{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    UserManager userManager;

    @Inject
    DatabaseManager databaseManager;

    private FragmentSquadBinding binding;
    private SquadViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private SquadAdapter squadAdapter;
    private EligibleAdapter eligibleAdapter;

    @Inject
    public SquadFragment() {
        //Required empty public constructor
    }

    public static SquadFragment getInstance() {
        return new SquadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(SquadViewModel.class);
        lifecycleOwner = this;

        viewModel.getSquad(userManager.getUser().getValue().getUid()).observe(this, squad ->{
            viewModel.findUsers(squad, userManager.getUser().getValue().getUid()).observe(lifecycleOwner, user ->{
                squadAdapter.setItems(user);
            });
        });

        loadData();

        eligibleAdapter.setLifeCycleOwner(lifecycleOwner);

        binding.setViewModelSquad(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_squad, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.friendsListForSquad.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.eligibleListForSquad.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        squadAdapter = new SquadAdapter(getContext(), databaseManager, userManager, getActivity().getSupportFragmentManager());
        eligibleAdapter = new EligibleAdapter(getContext(), databaseManager, userManager, getActivity().getSupportFragmentManager());

        binding.friendsListForSquad.setAdapter(squadAdapter);
        binding.eligibleListForSquad.setAdapter(eligibleAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override
    public void onClick(View v) {

    }

    private void loadData(){
        List<User> users = new ArrayList<>();

        viewModel.getFriends(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, friends ->{
            viewModel.findUsers(friends, userManager.getUser().getValue().getUid()).observe(lifecycleOwner, friendUsers -> {
                for(User user: friendUsers){
                    for(int i = 0; i < user.getFriends().size(); i++){
                        if(user.getFriends().get(i).contains(userManager.getUser().getValue().getUid())){
                            users.add(user);
                        }
                    }

                }
                eligibleAdapter.setItems(users);
            });
        });
    }
}
