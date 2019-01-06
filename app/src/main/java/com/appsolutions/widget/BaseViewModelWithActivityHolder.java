package com.appsolutions.widget;



import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.PropertyChangeRegistry;

/**
 * BaseViewModel for any ViewModel that needs to be Observable
 */

public abstract class BaseViewModelWithActivityHolder<M, R extends BaseRouter> extends ViewModelWithActivityHolder<R> implements Observable {
    private static final String TAG = BaseViewModelWithActivityHolder.class.getSimpleName();
    private PropertyChangeRegistry mRegistry = new PropertyChangeRegistry();
//    protected AppConfig mDataObject;
//    protected AppConfigRepository mAppConfigRepository;
    protected boolean mAppConfigWasLoading;
//    protected ConnectivityManager mConnectivityManager;
    protected M mModel;
    protected ObservableField<String> mSplashImageUrl = new ObservableField<>("");

    protected BaseViewModelWithActivityHolder() {}

    public abstract void resume();
    public void pause() {}
    public void destroy() {}

    protected void notifyPropertyChanged(Observable observable, int property) {
        mRegistry.notifyChange(observable, property);
    }

//    public boolean isDataObjectReady() {
//        return isDataObjectReady(false);
//    }
//
//    protected boolean isDataObjectReady(boolean notify) {
//        if (null != mDataObject) {
//            if (notify) {
//                notifyPropertyChanged(this, BR.appConfig);
//            }
//        }
//
//        return null != mDataObject;
//    }
//
//    @Bindable
//    public String getSplashImageUrl() { return mSplashImageUrl.get(); }
//
//    protected void setSplashImageUrl(String url) {
//        mSplashImageUrl.set(url);
//        notifyPropertyChanged(this, BR.splashImageUrl);
//    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        mRegistry.add(onPropertyChangedCallback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        mRegistry.remove(onPropertyChangedCallback);
    }

//    protected void takeAppConfig(DataObject<AppConfig> appConfigDataObject, OnPropertyChangedCallback callback) {
//        Log.d(TAG, "takeAppConfig wasLoading: " + mAppConfigWasLoading);
//        if (!appConfigDataObject.isLoading()) {
//            if (mAppConfigWasLoading) {
//                mAppConfigWasLoading = false;
//                mAppConfigRepository.removeOnPropertyChangedCallback(callback);
//                mDataObject = appConfigDataObject.getData();
//                if (mDataObject != null) {
//                    Log.d(TAG, "app config changed not null setting splash");
//                    setSplashImageUrl(mDataObject.getSplashImage());
//                    resume();
//                } else {
//                    Log.d(TAG, "app config was null");
//                }
//            } else {
//                Log.d(TAG, "app config changed wasn't loading was already clear");
//                if (appConfigDataObject.getData() != null) {
//                    mDataObject = appConfigDataObject.getData();
//                    setSplashImageUrl(mDataObject.getSplashImage());
//                }
//            }
//        } else {
//            Log.d(TAG, "app config changed is loading");
//        }
//    }
}
