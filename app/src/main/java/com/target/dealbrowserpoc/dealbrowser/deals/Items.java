package com.target.dealbrowserpoc.dealbrowser.deals;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Items extends BaseItem{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("aisle")
    @Expose
    private String aisle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("salePrice")
    @Expose
    private String salePrice;


    public Items(Parcel parcel){
        this.aisle = parcel.readString();
        this.description = parcel.readString();
        this.guid = parcel.readString();
        this.id = parcel.readString();
        this.image = parcel.readString();
        this.index = parcel.readInt();
        this.price = parcel.readString();
        this.salePrice = parcel.readString();
    }

    public Items() { super(); }

    public Items(String aisle, String description, String guid, String id, String image, int index, String price, String salePrice){
        this.aisle = aisle;
        this.description = description;
        this.guid = guid;
        this.id = id;
        this.image = image;
        this.index = index;
        this.price = price;
        this.salePrice = salePrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    @Override
    public int getItemType() {
        return BaseItem.DEALS_TYPE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getAisle());
        dest.writeString(getDescription());
        dest.writeString(getGuid());
        dest.writeString(getId());
        dest.writeInt(getIndex());
        dest.writeString(getImage());
        dest.writeString(getPrice());
        dest.writeString(getSalePrice());
    }

    public static final Parcelable.Creator<Items> CREATOR = new Parcelable.Creator<Items>(){

        @Override
        public Items createFromParcel(Parcel parcel) {
            return new Items(parcel);
        }

        @Override
        public Items[] newArray(int i) {
            return new Items[i];
        }
    };
}