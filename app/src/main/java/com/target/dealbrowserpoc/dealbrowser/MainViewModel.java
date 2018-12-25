package com.target.dealbrowserpoc.dealbrowser;

import com.target.dealbrowserpoc.dealbrowser.widget.BaseViewModel;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel<MainViewModel> {
    private static final String TAG = MainViewModel.class.getSimpleName();

    @Inject
    MainViewModel() {
        model = this;
    }

    @Override
    public void resume() {
//        feedRepo.initializeMainFeed();
    }
}
