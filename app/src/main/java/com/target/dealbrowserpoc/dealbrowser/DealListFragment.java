package com.target.dealbrowserpoc.dealbrowser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.target.dealbrowserpoc.dealbrowser.databinding.FragmentDealListBinding;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class DealListFragment extends DaggerFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    private FragmentDealListBinding binding;
    private DealListViewModel viewModel;
    private DealListItemAdapter dealListItemAdapter;

    @Inject
    public DealListFragment() {
        //Required empty public constructor
    }

    public static DealListFragment getInstance() {
        return new DealListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(DealListViewModel.class);

        binding.setViewModelDeal(viewModel);

        viewModel.resume();

        viewModel.getDealsItem().observe(this, dealItem -> {
            dealListItemAdapter.setItems(dealItem.getData());
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_deal_list, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        binding.dealList.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));

        dealListItemAdapter = new DealListItemAdapter(getContext(), picasso);

        binding.dealList.setAdapter(dealListItemAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

}