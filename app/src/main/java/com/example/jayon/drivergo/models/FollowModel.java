package com.example.jayon.drivergo.models;

/**
 * Created by MOHAMED on 10/30/2018.
 */

public class FollowModel {

    private  String me, transporter,id;

    public FollowModel(){}

    public FollowModel(String me, String transporter) {
        this.me = me;
        this.transporter = transporter;
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
