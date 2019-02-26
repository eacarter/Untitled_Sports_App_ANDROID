package com.appsolutions;

import android.util.Log;

import com.appsolutions.widget.BaseViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import androidx.annotation.IntDef;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends BaseViewModel<MainViewModel> {
    private static final String TAG = MainViewModel.class.getSimpleName();

    public static final int VIEW_HOME               = 0;
    public static final int VIEW_27                 = 1;
    public static final int VIEW_AUDIO              = 2;
    public static final int VIEW_VIDEO              = 3;
    public static final int VIEW_MORE               = 4;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VIEW_HOME, VIEW_27, VIEW_AUDIO, VIEW_VIDEO, VIEW_MORE,})
    @interface MainView {}

    private final MutableLiveData<Integer> mainView;


    @Inject
    MainViewModel() {
        model = this;
        mainView = new MutableLiveData<>();
        mainView.setValue(VIEW_HOME);
    }

    @Override
    public void resume() {
//        feedRepo.initializeMainFeed();
    }


    public LiveData<Integer> getMainView() {
        return mainView;
    }

    public void setMainView(@MainView int newMainView) {
//        if (newMainView == VIEW_PODCAST_PLAYER || newMainView == VIEW_PODCAST_DETAIL)
//            throw new IllegalArgumentException("PodcastFeed Player and PodcastFeed Detail view must be set with setPlayerView or setDetailView methods.");
//        currentPlayable.setValue(null);
//        currentDetailable.setValue(null);
//        startAuthWithRegister = false;
        mainView.setValue(newMainView);
    }

    public LiveData<Integer> getSelectedTab() {
        Log.d("selected", mainView.getValue().toString());
        return mainView;
    }

    void setSelectedTab(@MainView int selectedTab) {
        mainView.setValue(selectedTab);

    }
}
