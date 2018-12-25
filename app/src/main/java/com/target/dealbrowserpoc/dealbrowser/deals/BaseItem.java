package com.target.dealbrowserpoc.dealbrowser.deals;

import android.os.Parcelable;

import com.squareup.moshi.Json;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public abstract class BaseItem implements Parcelable {

    public static final int DEALS_TYPE = 0;

    @Json(name = "publisherId")
    protected String publisherId;
    @Json(name = "type")
    protected String type;
    @Json(name = "publishedAt")
    protected String publishedAt;
    @Json(name = "id")
    @PrimaryKey @NonNull
    protected String id = "";
    @Json(name = "location")
    protected String location;
    @Json(name = "title")
    protected String title;
    @Json(name = "sortorder")
    protected int sortOrder;

    public abstract int getItemType();

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
