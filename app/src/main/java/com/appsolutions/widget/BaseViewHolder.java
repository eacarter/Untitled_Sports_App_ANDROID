package com.appsolutions.widget;

import android.view.View;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Common base view holder, assumes variable name is always item.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    public interface BaseItemClickListener {
        void onItemClick(int position);
    }

    public final ViewDataBinding binding;

    public BaseViewHolder(ViewDataBinding binding, final BaseItemClickListener clickListener) {
        super(binding.getRoot());
        this.binding = binding;
        if (clickListener != null) {
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    /**
     * Binds the given item to the variable named item and executes the bindings. If overloading this
     * be sure to call super(item) last so the bindings are only executed once. If calling from another
     * set call, also be sure to call this last.
     * @param item The item to set as the bound item variable.
     */
    public void bind(Object item) {
//        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
    }
}
