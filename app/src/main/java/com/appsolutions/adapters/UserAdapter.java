package com.appsolutions.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsolutions.R;
import com.appsolutions.models.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> items, tempItems, suggestions;

    public UserAdapter(Context context, List<User> items) {
        super(context, R.layout.item_user_auto, items);
        this.items = items;
        this.context = context;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(R.layout.item_user_auto, parent, false);
            }
            User user = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.user_auto_text);
            ImageView imageView = view.findViewById(R.id.user_auto_image);

            Glide.with(getContext())
                    .load(user.getProfile_image())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);

            name.setText(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    @Nullable
    @Override
    public User getItem(int position) {
        return items.get(position);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }
    private Filter userFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            User user = (User) resultValue;
            return user.getUsername();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (User user: tempItems) {
                    if (user.getUsername().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(user);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<User> tempValues = (ArrayList<User>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (User user : tempValues) {
                    add(user);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}