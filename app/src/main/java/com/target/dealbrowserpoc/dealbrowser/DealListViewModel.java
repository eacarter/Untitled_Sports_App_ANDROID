package com.target.dealbrowserpoc.dealbrowser;

import com.target.dealbrowserpoc.dealbrowser.deals.DealItem;
import com.target.dealbrowserpoc.dealbrowser.repo.DealsRepo;
import com.target.dealbrowserpoc.dealbrowser.widget.BaseViewModel;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;

public class DealListViewModel extends BaseViewModel {

    private DealsRepo dealsRepo;

    @Inject
    public DealListViewModel(DealsRepo dealsRepo){
        this.dealsRepo = dealsRepo;
    }

    public LiveData<DealItem> getDealsItem(){
        return dealsRepo.getDeals();
    }

    @Override
    public void resume() {

    }
}
