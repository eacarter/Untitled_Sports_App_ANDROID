package com.target.dealbrowserpoc.dealbrowser.repo;

import android.util.Log;

import com.target.dealbrowserpoc.dealbrowser.deals.DealItem;
import com.target.dealbrowserpoc.dealbrowser.network.WebService;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealsRepo {

    @Inject
    WebService webService;

    private MutableLiveData<DealItem> liveDeals;

    @Inject
    public DealsRepo(){

    }

    public LiveData<DealItem> getDeals(){
        MutableLiveData<DealItem> deals = liveDeals;

        if(deals == null) {
            deals = new MutableLiveData<>();
            liveDeals = deals;

            Call<DealItem> dealsCall = webService.loadDealItems();

            dealsCall.enqueue(new Callback<DealItem>() {
                @Override
                public void onResponse(Call<DealItem> call, Response<DealItem> response) {
                    if(response.isSuccessful() && response.body() != null){
                        MutableLiveData<DealItem> deals = liveDeals;
                        DealItem dealItem = response.body();
                        deals.setValue(dealItem);
                    }
                    else{
                        Log.d("Response", response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<DealItem> call, Throwable t) {
                    Log.d("Response", t.toString());
                }
            });
        }
        return deals;
    }
}
