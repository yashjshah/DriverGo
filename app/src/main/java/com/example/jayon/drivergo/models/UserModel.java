package com.example.jayon.drivergo.models;

import java.io.Serializable;

/**
 * Created by MOHAMED on 10/26/2018.
 */

public class UserModel implements Serializable {

    private String image,sammary,firstName,lastName,email,phone,date,address,role,typeOfVehicule,capacity,id,password,latitude,longitute;

    public UserModel(){}

    public UserModel(String image,String sammary,String firstName, String lastName, String email, String phone, String date, String address,String latitude,String longitute, String role , String typeOfVehicule, String capacity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.date = date;
        this.address = address;
        this.role = role;
        this.sammary=sammary;
        this.typeOfVehicule = typeOfVehicule;
        this.capacity = capacity;
        this.image=image;
        this.latitude=latitude;
        this.longitute=longitute;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLongitute() {
        return longitute;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSammary() {
        return sammary;
    }

    public void setSammary(String sammary) {
        this.sammary = sammary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public String getTypeOfVehicule() {
        return typeOfVehicule;
    }

    public void setTypeOfVehicule(String typeOfVehicule) {
        this.typeOfVehicule = typeOfVehicule;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
}
