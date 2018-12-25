package com.target.dealbrowserpoc.dealbrowser.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class DealViewHolder extends BindingViewholder {

    private ViewDataBinding binding;
    private ImageView imageView;
    private TextView textView;

    public DealViewHolder(ViewDataBinding binding) {
        super(binding);
        this.binding = DataBindingUtil.bind(itemView);
    }

    public DealViewHolder(View itemView) {
        super(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setImageView(ImageView view) {
        imageView = view;
    }

    public ImageView getImageView() { return imageView; }

    public void setTextView(TextView view){ textView = view; }

    public TextView getTextView() {return textView;}
}
