package com.target.dealbrowserpoc.dealbrowser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.target.dealbrowserpoc.dealbrowser.databinding.FragmentDealItemBinding;
import com.target.dealbrowserpoc.dealbrowser.databinding.FragmentDealListBinding;
import com.target.dealbrowserpoc.dealbrowser.deals.DealItem;
import com.target.dealbrowserpoc.dealbrowser.deals.Items;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerFragment;

public class DealItemFragment extends DaggerFragment implements View.OnClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Picasso picasso;

    private static final String ARG_DEALS = "ARG_DEALS";
    private FragmentDealItemBinding binding;
    private DealItemViewModel viewModel;
    private Items items;

    @Inject
    public DealItemFragment() {
        //Required empty public constructor
    }

    public static DealItemFragment getInstance(Items item) {
        DealItemFragment fragment = new DealItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DEALS, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(DealItemViewModel.class);

        binding.setViewModelItem(viewModel);

        viewModel.resume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_deal_item, container, false);
        binding.executePendingBindings();
        binding.setLifecycleOwner(this);

        Bundle args = getArguments();

        items = args.getParcelable(ARG_DEALS);

        binding.dealItemAddCart.setOnClickListener(this);
        binding.dealItemAddList.setOnClickListener(this);

        picasso.load(items.getImage()).fit().placeholder(R.drawable.ic_launcher).into(binding.dealItemImage);
        binding.dealItemTitle.setText(items.getTitle());
        binding.dealItemDesc.setText(items.getDescription());

        if(items.getSalePrice() == null){
            binding.dealItemPrice.setText(items.getPrice());
            binding.dealItemReg.setVisibility(View.INVISIBLE);
            binding.dealItemRegTag.setVisibility(View.INVISIBLE);
            binding.regDivider.setVisibility(View.INVISIBLE);
        }
        else{
            binding.dealItemReg.setText(items.getPrice());
            binding.dealItemPrice.setText(items.getSalePrice());
        }

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.resume();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.dealItemAddList.getId()){
            Toast.makeText(getContext(), "Item added to list", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == binding.dealItemAddCart.getId()){
            Toast.makeText(getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
        }
    }
}