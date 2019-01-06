package com.appsolutions.widget;

import com.appsolutions.MainViewModel;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.ViewModel;


/**
 * BaseViewModel for any ViewModel that needs to be Observable
 */

public abstract class BaseViewModel<M> extends ViewModel implements Observable {
    private static final String TAG = BaseViewModel.class.getSimpleName();
    private PropertyChangeRegistry registry = new PropertyChangeRegistry();
    protected M model;
    protected MainViewModel mainViewModel;

    public BaseViewModel() { }

    public abstract void resume();
    public void pause() {}
    public void destroy() {}

    public void setMainViewModel(MainViewModel mainViewModel) {
        this.mainViewModel = mainViewModel;
    }

    protected void notifyPropertyChanged(Observable observable, int property) {
        registry.notifyChange(observable, property);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        registry.add(onPropertyChangedCallback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        registry.remove(onPropertyChangedCallback);
    }
}
