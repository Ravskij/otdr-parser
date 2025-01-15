package data;

import java.util.*;

public class FxdParams {
    Date date; // Дата и время измерения
    long wavelength; // Длина волны
    String distanceUnits; // Единицы измерения расстояния

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getWavelength() {
        return wavelength;
    }

    public void setWavelength(long wavelength) {
        this.wavelength = wavelength;
    }

    public String getDistanceUnits() {
        return distanceUnits;
    }

    public void setDistanceUnits(String distanceUnits) {
        this.distanceUnits = distanceUnits;
    }

    @Override
    public String toString() {
        return "FxdParams{" +
                "date=" + date +
                ", wavelength=" + wavelength +
                ", distanceUnits='" + distanceUnits + '\'' +
                '}';
    }

}
