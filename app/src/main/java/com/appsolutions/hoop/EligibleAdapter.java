package com.appsolutions.hoop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.SquadItemBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Notifications;
import com.appsolutions.models.User;
import com.appsolutions.profile.ProfileThirdFragment;
import com.appsolutions.widget.BindingViewholder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public class EligibleAdapter extends RecyclerView.Adapter<BindingViewholder> {

    private static final int IMAGE_TYPE = 0;
    private static final int MESSAGE_TYPE = 1;
    private Context context;
    private List<User> items;
    private DatabaseManager databaseManager;
    private UserManager userManager;
    private LifecycleOwner lifecycleOwner;
    private FragmentManager fragmentManager;

    @Inject
    public EligibleAdapter(Context context, DatabaseManager databaseManager, UserManager userManager,
                           FragmentManager fragmentManager){
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
                R.layout.squad_item, parent, false);
        binding.executePendingBindings();
        binding.getRoot();
        return new BindingViewholder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewholder holder, int position) {
        User item = items.get(position);

            SquadItemBinding binding = (SquadItemBinding) holder.getBinding();

        databaseManager.getSquad(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, squadList ->{
            if(squadList.size() <= 5) {
                if (squadList.contains(item.getId())) {
                    binding.fabText.setText("Remove");
                } else {
                    binding.fabText.setText("Add to Squad");
                }
            }
        });

            Glide.with(context)
                    .load(item.getProfile_image())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.userImage);

            binding.userName.setText(item.getUsername());
            binding.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseManager.getUser(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, user -> {

                        databaseManager.getSquad(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, squadList ->{
                            if(squadList.size() <= 5) {
                                if (squadList.contains(item.getId())) {
                                    databaseManager.removeSquad(userManager.getUser().getValue(), item.getId());
                                    Notifications notify = new Notifications();
                                    notify.setId(item.getId()+""+randomGenerator());
                                    notify.setImage(user.getProfile_image());
                                    notify.setMessage(user.getUsername()+" has removed you to their squad.");
                                    notify.setTimeStamp(new Date().getTime());
                                    notify.setType("squad");
                                    databaseManager.addNotification(item.getId(), notify.getId(), notify);
                                    binding.fabText.setText("Add to Squad");
                                } else {
                                    databaseManager.addSquad(userManager.getUser().getValue(), item.getId());
                                    Notifications notify = new Notifications();
                                    notify.setId(item.getId()+""+randomGenerator());
                                    notify.setImage(user.getProfile_image());
                                    notify.setMessage(user.getUsername()+" has added you to their squad.");
                                    notify.setTimeStamp(new Date().getTime());
                                    notify.setType("squad");
                                    databaseManager.addNotification(item.getId(), notify.getId(), notify);
                                    binding.fabText.setText("Remove");
                                }
                            }
                        });
                    });
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
       items = item;
       notifyDataSetChanged();
    }

    public void setLifeCycleOwner(LifecycleOwner lifeCycleOwner){
        this.lifecycleOwner = lifeCycleOwner;
    }

    @Override
    public int getItemCount() {
        return items.size();
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
