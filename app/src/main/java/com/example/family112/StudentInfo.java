package com.example.family112;

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
}
