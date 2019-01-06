package com.appsolutions.widget;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dante on 11/8/17.
 * So we don't have to make a databinding ViewHolder for each Adapter
 *
 * STOLEN BY KYLE 6/8/18
 */

public class BindingViewholder extends RecyclerView.ViewHolder {

    private ViewDataBinding binding;

    public BindingViewholder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = DataBindingUtil.bind(itemView);
    }

    public BindingViewholder(View itemView) {
        super(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}
