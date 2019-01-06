package com.appsolutions.util;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

/**
 * Wraps a {@link LiveData} {@link Number} in an {@link ObservableInt}.
 * Created by benlevy on 12/28/17.
 */

public class LiveDataObservableInt extends ObservableInt {
    public LiveDataObservableInt(LifecycleOwner lifecycleOwner, LiveData<? extends Number> liveData) {
        this(lifecycleOwner, liveData, 0);
    }

    public LiveDataObservableInt(LifecycleOwner lifecycleOwner, LiveData<? extends Number> liveData, int defaultValue) {
        liveData.observe(lifecycleOwner, value -> set(value != null ? value.intValue() : defaultValue));
    }
}
