package com.target.dealbrowserpoc.dealbrowser.widget;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

/**
 * {@link ViewModel} class that can take a navigation router.
 */

public abstract class ViewModelWithActivityHolder<T extends BaseRouter> extends ViewModel {
    protected ActivityHolder<T> mActivityHolder;

    public void setActivityHolder(ActivityHolder<T> activityHolder) {
        mActivityHolder = activityHolder;
    }

    @Nullable
    protected T getRouter() {
        return mActivityHolder != null ? mActivityHolder.getRouter() : null;
    }
}
