package com.appsolutions.notification;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.NotificationItemBinding;
import com.appsolutions.databinding.SquadItemBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.appsolutions.models.Notifications;
import com.appsolutions.models.User;
import com.appsolutions.widget.BindingViewholder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<BindingViewholder> {

    private static final int IMAGE_TYPE = 0;
    private static final int MESSAGE_TYPE = 1;
    private Context context;
    private List<Notifications> items;
    private DatabaseManager databaseManager;
    private UserManager userManager;
    private LifecycleOwner lifecycleOwner;
    private FragmentManager fragmentManager;

    @Inject
    public NotificationAdapter(Context context, DatabaseManager databaseManager, UserManager userManager){
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
                R.layout.notification_item, parent, false);
        binding.executePendingBindings();
        binding.getRoot();
        return new BindingViewholder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewholder holder, int position) {
        Notifications item = items.get(position);

            NotificationItemBinding binding = (NotificationItemBinding) holder.getBinding();

            Glide.with(context)
                    .load(item.getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.userImage);

            binding.userName.setText(item.getMessage());
            binding.userTime.setText(new SimpleDateFormat("MM/dd/yyyy").format(new Date(item.getTimeStamp())));
//            binding.fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    databaseManager.getSquad(userManager.getUser().getValue().getUid()).observe(lifecycleOwner, squadList ->{
//                        if(squadList.size() <= 5) {
//                            if (squadList.contains(item.getId())) {
//                                databaseManager.removeSquad(userManager.getUser().getValue(), item.getId());
//                                Notifications notify = new Notifications();
//                                notify.setId(item.getId()+""+randomGenerator());
//                                notify.setImage(item.getProfile_image());
//                                notify.setMessage(item.getUsername()+" has removed you to their squad.");
//                                notify.setTimeStamp(new Date().getTime());
//                                notify.setType("squad");
//                                databaseManager.addNotification(item.getId(), notify.getId(), notify);
//                                binding.fabText.setText("Add to Squad");
//                            } else {
//                                databaseManager.addSquad(userManager.getUser().getValue(), item.getId());
//                                Notifications notify = new Notifications();
//                                notify.setId(item.getId()+""+randomGenerator());
//                                notify.setImage(item.getProfile_image());
//                                notify.setMessage(item.getUsername()+" has added you to their squad.");
//                                notify.setTimeStamp(new Date().getTime());
//                                notify.setType("squad");
//                                databaseManager.addNotification(item.getId(), notify.getId(), notify);
//                                binding.fabText.setText("Remove");
//                            }
//                        }
//                    });
//                }
//            });

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

    public void setItems(List<Notifications> item){
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

}
