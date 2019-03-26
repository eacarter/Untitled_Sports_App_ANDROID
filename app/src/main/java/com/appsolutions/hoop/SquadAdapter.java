package com.appsolutions.hoop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.SquadItemBinding;
import com.appsolutions.databinding.SquadListItemBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.User;
import com.appsolutions.profile.ProfileThirdFragment;
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

public class SquadAdapter extends RecyclerView.Adapter<BindingViewholder> {

    private static final int IMAGE_TYPE = 0;
    private static final int MESSAGE_TYPE = 1;
    private Context context;
    private List<User> items;
    private DatabaseManager databaseManager;
    private UserManager userManager;
    private FragmentManager fragmentManager;

    @Inject
    public SquadAdapter(Context context, DatabaseManager databaseManager, UserManager userManager, FragmentManager fragmentManager){
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

        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.squad_list_item, parent, false);
        binding.executePendingBindings();
        binding.getRoot();
        return new BindingViewholder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewholder holder, int position) {
        User item = items.get(position);

            SquadListItemBinding binding = (SquadListItemBinding) holder.getBinding();

            Glide.with(context)
                    .load(item.getProfile_image())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.profileImage);

            binding.profileId.setText(item.getUsername());
            binding.profileClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    fragmentManager.beginTransaction()
                            .add(R.id.container, ProfileThirdFragment.getInstance(item.getId()))
                            .addToBackStack("")
                            .commit();
//                    feedItem(item.getId(), randomGenerator()).show();
                }
            });

    }

    @Override
    public int getItemViewType(int position){
//        if(items.get(position).getMessageImage().isEmpty() || items.get(position).getMessageImage() == null){
//            return MESSAGE_TYPE;
//        }
//        else if(items.get(position).getMessage().isEmpty() || items.get(position).getMessage() == null){
//            return IMAGE_TYPE;
//        }
        return -1;
    }

    public void setItems(List<User> item){
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
