package com.example.jayon.drivergo.models;

import java.io.Serializable;

/**
 * Created by MOHAMED on 10/26/2018.
 */

public class OfferModel implements Serializable {

    private String id,description,date,price,offerId;

    public OfferModel() {
    }

    public OfferModel(String id, String description, String date, String price) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.price = price;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
