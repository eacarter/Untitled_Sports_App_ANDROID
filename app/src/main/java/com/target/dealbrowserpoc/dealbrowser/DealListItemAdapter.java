package com.target.dealbrowserpoc.dealbrowser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;
import com.target.dealbrowserpoc.dealbrowser.databinding.DealListItemBinding;
import com.target.dealbrowserpoc.dealbrowser.deals.BaseItem;
import com.target.dealbrowserpoc.dealbrowser.deals.DealItem;
import com.target.dealbrowserpoc.dealbrowser.deals.Items;
import com.target.dealbrowserpoc.dealbrowser.widget.BindingViewholder;
import com.target.dealbrowserpoc.dealbrowser.widget.DealViewHolder;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class DealListItemAdapter <V extends BaseItem> extends RecyclerView.Adapter<BindingViewholder> {

    private Context context;
    private Picasso picasso;
    private List<Items> items;

    @Inject
    public DealListItemAdapter(Context context, Picasso picasso){
        this.context = context;
        this.picasso = picasso;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public BindingViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;

        if(viewType == BaseItem.DEALS_TYPE) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.deal_list_item, parent, false);
            return new DealViewHolder(binding);
        }
        else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewholder holder, int position) {
        Items item = items.get(position);
        DealViewHolder viewHolder = (DealViewHolder) holder;
        DealListItemBinding binding = (DealListItemBinding) viewHolder.getBinding();
        binding.executePendingBindings();

        binding.dealListItemIsle.setText(item.getAisle());
        binding.dealListItemPrice.setText(item.getPrice());
        binding.dealListItemTitle.setText(item.getTitle());
        picasso.load(item.getImage()).placeholder(R.drawable.ic_launcher).fit().into(binding.dealListItemImageView);

        binding.dealListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) context).getSupportFragmentManager().
                        beginTransaction().
                        add(R.id.container, DealItemFragment.getInstance(item), "DealItemFragment").
                        addToBackStack("back").
                        commit();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position){
        if(items.get(position) instanceof  Items){
            return BaseItem.DEALS_TYPE;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public <T extends V> void setItems(List<Items> item) {
        items = item;
        notifyDataSetChanged();
    }
}
