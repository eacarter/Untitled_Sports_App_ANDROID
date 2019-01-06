package com.appsolutions.widget;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * Class to allow access to Activity without retaining reference to Activity after destroyed.
 * Created by benlevy on 1/3/18.
 */

public class ActivityHolder<T extends BaseRouter> implements DefaultLifecycleObserver {
    private T router;
    private Activity activity;

    public ActivityHolder(@NonNull LifecycleOwner lifecycleOwner, @Nullable ViewModelWithActivityHolder<T> viewModel, @NonNull Activity activity, @Nullable T router) {
        this.router = router;
        this.activity = activity;
        if (viewModel != null)
            viewModel.setActivityHolder(this);
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        activity = null;
        router = null;
    }

    @Nullable
    public T getRouter() {
        return router;
    }

    @Nullable
    public Activity getActivity() {
        return activity;
    }
}
