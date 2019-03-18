package com.appsolutions.feed;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentCommentBinding;
import com.appsolutions.databinding.FragmentFeedBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Feed;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dagger.android.support.DaggerFragment;

public class CommentFragment extends DaggerFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    UserManager userManager;

    @Inject
    DatabaseManager databaseManager;

    private FragmentCommentBinding binding;
    private CommentViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private CommentAdapter commentAdapter;
    private String feedItem;
    private Feed feed;
    private String username;
    private String userPhotoUrl;


    @Inject
    public CommentFragment() {
        //Required empty public constructor
    }

    public static CommentFragment getInstance(String feedItem) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("item",feedItem);
        commentFragment.setArguments(args);
        return commentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(CommentViewModel.class);
        lifecycleOwner = this;

        if(getArguments() != null){
            feedItem = getArguments().getString("item");
            Gson gson = new Gson();
            Type token = new TypeToken<Feed>(){}.getType();
            feed = gson.fromJson(feedItem,token);
        }

        viewModel.getUser().observe(this, user -> {

            viewModel.getUserDB(user.getUid());
            databaseManager.getUser(user.getUid()).observe(this, user1 -> {
                username = user.getEmail().split("@")[0];
                userPhotoUrl = user1.getProfile_image();
            });
        });

        Glide.with(getContext())
                .load(feed.getUserPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.feedUserImage);

        binding.feedContent.setText(feed.getMessage());
        binding.feedUserName.setText(feed.getUsername());

        binding.commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postId = randomGenerator();

                Feed feed2 = new Feed();
                feed2.setId(postId);
                feed2.setMessage(binding.commentInput.getText().toString());
                feed2.setUsername(username);
                feed2.setUserPicture(userPhotoUrl);
                feed2.setTimeStamp(new Date().getTime());
                viewModel.sendCommentItem(feed.getId(), postId, feed2 );
                binding.commentInput.setText("");
                binding.refresher.setRefreshing(true);
                loadData();
            }
        });

        binding.refresher.post(new Runnable() {
            @Override
            public void run() {
                binding.refresher.setRefreshing(true);
                loadData();
            }
        });

        binding.setViewModelComment(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_comment, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.feedComments.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        binding.refresher.setOnRefreshListener(this);

        commentAdapter = new CommentAdapter(getContext(), databaseManager, userManager, getActivity().getSupportFragmentManager());

        binding.feedComments.setAdapter(commentAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    public void loadData(){
        viewModel.getCommentItems(feed.getId()).observe(lifecycleOwner, feed ->{
            commentAdapter.setItems(feed);
            binding.refresher.setRefreshing(false);
        });
    }

    @Override
    public void onRefresh() {
        loadData();
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
}
