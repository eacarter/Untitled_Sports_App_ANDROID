package com.appsolutions.feed;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.appsolutions.R;
import com.appsolutions.adapters.UserAdapter;
import com.appsolutions.databinding.FragmentFeedBinding;
import com.appsolutions.databinding.FragmentNotifBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Feed;
import com.appsolutions.models.User;
import com.appsolutions.profile.ProfileThirdFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dagger.android.support.DaggerFragment;

public class FeedFragment extends DaggerFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    UserManager userManager;

    @Inject
    DatabaseManager databaseManager;

    private FragmentFeedBinding binding;
    private FeedViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private FeedAdapter feedAdapter;
    private List<String> friendIDs;
    private List<Feed> feedITEMS;


    @Inject
    public FeedFragment() {
        //Required empty public constructor
    }

    public static FeedFragment getInstance() {
        return new FeedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(FeedViewModel.class);
        lifecycleOwner = this;

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedItem(userManager.getUser().getValue().getUid() + randomGenerator()).show();
            }
        });

        binding.refresher.post(new Runnable() {
            @Override
            public void run() {
                binding.refresher.setRefreshing(true);
                loadData();
            }
        });

        databaseManager.getUserItems().observe(this, users -> {

            UserAdapter adapter = new UserAdapter(getContext(), users);
            binding.userSearch.setThreshold(2);
            binding.userSearch.setAdapter(adapter);
            binding.userSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User user = (User) parent.getItemAtPosition(position);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, ProfileThirdFragment.getInstance(user.getId()))
                            .addToBackStack("")
                            .commit();
                    binding.userSearch.setText("");
                }
            });
        });

        binding.setViewModelFeed(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_feed, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.feedList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        binding.refresher.setOnRefreshListener(this);

        feedAdapter = new FeedAdapter(getContext(), databaseManager, userManager, getActivity().getSupportFragmentManager());

        binding.feedList.setAdapter(feedAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    public void loadData(){

        List<Feed> newFeed = new ArrayList<>();

        if(userManager.getUser().getValue() == null){

        }
        else {
            viewModel.getFeedItems(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, feed -> {
                if (feed != null) {
                    feedITEMS = feed;
                    viewModel.getFriends(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, users -> {
                        if (users != null) {
                            friendIDs = users;

                            for (int j = 0; j < friendIDs.size(); j++) {
                                for (int i = 0; i < feedITEMS.size(); i++) {
                                    if (feedITEMS.get(i).getId().contains(friendIDs.get(j))) {
                                        newFeed.add(feedITEMS.get(i));
                                    }
                                }
                            }
                            feedAdapter.setItems(newFeed);
                            binding.refresher.setRefreshing(false);
                        }
                    });
                }
            });
        }
    }

    public AlertDialog feedItem(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.dialog_feed, null);
        builder.setView(view);
        builder.setMessage("Enter your feed message");
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                viewModel.getUserId(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, user -> {

                        EditText text = (EditText) view.findViewById(R.id.message_post);

                        Feed feed = new Feed();
                        feed.setId(id);
                        feed.setMessage(text.getText().toString());
                        feed.setUsername(user.getUsername());
                        feed.setUserPicture(user.getProfile_image());
                        feed.setTimeStamp(new Date().getTime());
                        viewModel.uploadFeedItem(id, feed);
                        binding.refresher.setRefreshing(true);
                        loadData();
                });
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

    @Override
    public void onRefresh() {
        loadData();
    }
}
