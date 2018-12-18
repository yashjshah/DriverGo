package com.example.jayon.drivergo.models;

/**
 * Created by MOHAMED on 10/29/2018.
 */

public class SubscribeModel {

    private  String me, transporter,status,from,to,offerID,id;

    public SubscribeModel(){}

    public SubscribeModel(String offerID,String me, String transporter, String status,String from,String to) {
        this.me = me;
        this.transporter = transporter;
        this.status = status;
        this.offerID=offerID;
        this.from=from;
        this.to=to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
