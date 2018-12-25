package com.target.dealbrowserpoc.dealbrowser.network;

import com.target.dealbrowserpoc.dealbrowser.deals.DealItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebService {

    String ITEMS_URL = "http://target-deals.herokuapp.com/";
    String ITEMS_DEALS = "api/deals";

    @GET(ITEMS_DEALS)
    Call<DealItem> loadDealItems();
}
