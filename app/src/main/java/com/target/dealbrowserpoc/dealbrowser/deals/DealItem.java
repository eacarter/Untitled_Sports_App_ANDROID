package com.target.dealbrowserpoc.dealbrowser.deals;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealItem {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("data")
    @Expose
    private List<Items> data = null;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Items> getData() {
        return data;
    }

    public void setData(List<Items> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}