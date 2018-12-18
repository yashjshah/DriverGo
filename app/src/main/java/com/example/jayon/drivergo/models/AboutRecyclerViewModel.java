package com.example.jayon.drivergo.models;


public class AboutRecyclerViewModel {

    int icon;
    String header , value;

    public AboutRecyclerViewModel(int icon, String header, String value) {
        this.icon = icon;
        this.header = header;
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public String getHeader() {
        return header;
    }

    public String getValue() {
        return value;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
