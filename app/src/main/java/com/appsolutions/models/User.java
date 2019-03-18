package com.appsolutions.models;

import java.util.Arrays;
import java.util.List;

public class User {

    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String city;
    private String state;
    private String zipcode;
    private String height;
    private String weight;
    private String age;
    private String about;
    private String profile_image;
    private String gender;
    private String dominant_hand;
    private double latitude;
    private double longitude;
    private boolean medic_info;
    private List<String> friends;
    private List<String> endorsements;
    private int Average_Rating;
    private List<String> squad;

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getAverage_Rating() {
        return Average_Rating;
    }

    public void setAverage_Rating(int average_Rating) {
        Average_Rating = average_Rating;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDominant_hand() {
        return dominant_hand;
    }

    public void setDominant_hand(String dominant_hand) {
        this.dominant_hand = dominant_hand;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isMedic_info() {
        return medic_info;
    }

    public void setMedic_info(boolean medic_info) {
        this.medic_info = medic_info;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(List<String> endorsments) {
        this.endorsements = endorsments;
    }

    public List<String> getSquad() {
        return squad;
    }

    public void setSquad(List<String> squad) {
        this.squad = squad;
    }
}
