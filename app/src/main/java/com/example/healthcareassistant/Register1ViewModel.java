package com.example.healthcareassistant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class Register1ViewModel extends AndroidViewModel {

    private String name;
    private String mspNum;
    private String gender;
    private String availability;
    private String department;
    private String email;
    private String password;

    public Register1ViewModel(@NonNull Application application) {
        super(application);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
