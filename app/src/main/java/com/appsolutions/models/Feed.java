package com.appsolutions.models;

import com.google.firebase.Timestamp;

public class Feed {

    private String id;
    private String username;
    private String userPicture;
    private String message;
    private String messageImage;
    private Long timeStamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public void setMessageImage(String messageImage) {
        this.messageImage = messageImage;
    }

    public Long getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp){
        this.timeStamp = timeStamp;
    }
}
