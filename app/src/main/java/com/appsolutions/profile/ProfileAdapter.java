package com.appsolutions.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FeedItemImageBinding;
import com.appsolutions.databinding.FeedItemMessageBinding;
import com.appsolutions.feed.CommentFragment;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Feed;
import com.appsolutions.widget.BindingViewholder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<BindingViewholder> {

    private static final int IMAGE_TYPE = 0;
    private static final int MESSAGE_TYPE = 1;
    private Context context;
    private List<Feed> items;
    private DatabaseManager databaseManager;
    private UserManager userManager;
    private FragmentManager fragmentManager;

    @Inject
    public ProfileAdapter(Context context, DatabaseManager databaseManager, UserManager userManager, FragmentManager fragmentManager){
        this.context = context;
        this.databaseManager = databaseManager;
        this.userManager = userManager;
        this.fragmentManager = fragmentManager;
        items = new ArrayList<>();
    }


    @NonNull
    @Override
    public BindingViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;

        if(viewType == IMAGE_TYPE){
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.feed_item_image, parent, false);
            binding.executePendingBindings();
            binding.getRoot();
            return new BindingViewholder(binding);
        }
        else{
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.feed_item_message, parent, false);
            binding.executePendingBindings();
            binding.getRoot();
            return new BindingViewholder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewholder holder, int position) {
        Feed item = items.get(position);

        if(item.getMessage().isEmpty()){
            FeedItemImageBinding binding = (FeedItemImageBinding) holder.getBinding();

            Glide.with(context)
                    .load(item.getUserPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.feedUserImage);

            Glide.with(context)
                    .load(item.getMessageImage())
                    .apply(RequestOptions.centerInsideTransform())
                    .into(binding.feedContent);

            binding.feedUserName.setText(item.getUsername());
        }
        else{
            FeedItemMessageBinding binding = (FeedItemMessageBinding) holder.getBinding();

            Glide.with(context)
                    .load(item.getUserPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.feedUserImage);

            binding.feedContent.setText(item.getMessage());
            binding.feedUserName.setText(item.getUsername());
            binding.feedComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();

                    fragmentManager.beginTransaction()
                            .add(R.id.container, CommentFragment.getInstance(gson.toJson(item)))
                            .addToBackStack("")
                            .commit();
//                    feedItem(item.getId(), randomGenerator()).show();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position){
//        if(items.get(position).getMessageImage().isEmpty() || items.get(position).getMessageImage() == null){
            return MESSAGE_TYPE;
//        }
//        else if(items.get(position).getMessage().isEmpty() || items.get(position).getMessage() == null){
//            return IMAGE_TYPE;
//        }
//        return -1;
    }

    public void setItems(List<Feed> item){
        if(item.size() < 5){
            items = item.subList(0, item.size());
        }
        else {
            items = item.subList(0, 5);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}