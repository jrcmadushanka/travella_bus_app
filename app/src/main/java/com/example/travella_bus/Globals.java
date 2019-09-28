package com.example.travella_bus;

import android.app.Application;

public class Globals extends Application {

    private String vehicleID;

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }
}
