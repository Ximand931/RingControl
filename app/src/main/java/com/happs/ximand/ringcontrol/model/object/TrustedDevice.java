package com.happs.ximand.ringcontrol.model.object;

import androidx.annotation.NonNull;

import java.util.Objects;

public class TrustedDevice {

    private int id;
    private String manufacturer;
    private String model;
    private String date;

    public TrustedDevice(int id, String manufacturer, String model, String date) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrustedDevice that = (TrustedDevice) o;
        return id == that.id &&
                Objects.equals(manufacturer, that.manufacturer) &&
                Objects.equals(model, that.model) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manufacturer, model, date);
    }

    @NonNull
    @Override
    public String toString() {
        return "TrustedDevice{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
