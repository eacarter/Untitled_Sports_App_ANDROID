package com.appsolutions.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentNotifBinding;
import com.appsolutions.manager.DatabaseManager;
import com.appsolutions.manager.UserManager;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class NotifFragment extends DaggerFragment{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    UserManager userManager;

    @Inject
    DatabaseManager databaseManager;

    private FragmentNotifBinding binding;
    private NotifViewModel viewModel;
    private NotificationAdapter notificationAdapter;
    private LifecycleOwner lifecycleOwner;

    @Inject
    public NotifFragment() {
        //Required empty public constructor
    }

    public static NotifFragment getInstance() {
        return new NotifFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(NotifViewModel.class);
        lifecycleOwner = this;

        viewModel.getNotifications(userManager.getUser().getValue().getUid()).observe(this, noti ->{
            notificationAdapter.setItems(noti);
        });

        binding.setViewModelNotif(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_notif, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.notifList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(getContext(), databaseManager, userManager);
        binding.notifList.setAdapter(notificationAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }
}
