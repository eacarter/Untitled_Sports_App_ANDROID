package com.appsolutions.hoop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsolutions.R;
import com.appsolutions.databinding.FragmentFeedBinding;
import com.appsolutions.databinding.FragmentHoopBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.DaggerFragment;

public class HoopFragment extends DaggerFragment implements View.OnClickListener{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    private FragmentHoopBinding binding;
    private HoopViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    @Inject
    public HoopFragment() {
        //Required empty public constructor
    }

    public static HoopFragment getInstance() {
        return new HoopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(HoopViewModel.class);
        lifecycleOwner = this;

        binding.createGameTab.setOnClickListener(this);

        binding.squadGameTab.setOnClickListener(this);

        binding.setViewModelHoop(viewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_hoop, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.createGameTab.getId()){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, CreateGameFragment.getInstance())
                    .addToBackStack("create")
                    .commit();
        }
        if(v.getId() == binding.squadGameTab.getId()){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, SquadFragment.getInstance())
                    .addToBackStack("squad")
                    .commit();
        }
    }
}
