package com.example.family112;

import com.amap.api.maps2d.model.LatLng;

public class StudentInfo {
    /**
     * A simple struct for storing the info of out classmates.
     */
    private final int id, studentNumber;
    private final String name;
    private final String city;
    private final String university;
    private final double longitude, latitude;

    public StudentInfo(int id, int studentNumber, String name, String city, String university, double longitude, double latitude) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.name = name;
        this.city = city;
        this.university = university;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "id=" + id +
                ", studentNumber=" + studentNumber +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", university='" + university + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}' + '\n';
    }

    public LatLng getLatLng(){
        return new LatLng(latitude, longitude);
    }
}
