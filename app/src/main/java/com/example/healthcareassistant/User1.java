package com.example.healthcareassistant;

public class User1 {

    private String name;
    private String mspNum;
    private String gender;
    private String availability;
    private String department;
    private String email;

    public User1() {

    }

    public User1(String name, String mspNum, String gender, String availability, String department, String email) {
        this.name = name;
        this.mspNum = mspNum;
        this.gender = gender;
        this.availability = availability;
        this.department = department;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMspNum() {
        return mspNum;
    }

    public void setMspNum(String mspNum) {
        this.mspNum = mspNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
