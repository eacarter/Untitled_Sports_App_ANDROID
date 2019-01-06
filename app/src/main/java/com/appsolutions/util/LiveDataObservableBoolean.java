package com.appsolutions.util;


import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

/**
 * Wraps a {@link LiveData} {@link Number} in an {@link ObservableInt}.
 */

public class LiveDataObservableBoolean extends ObservableBoolean {
    public LiveDataObservableBoolean(LifecycleOwner lifecycleOwner, LiveData<Boolean> liveData) {
        liveData.observe(lifecycleOwner, this::set);
    }
}
