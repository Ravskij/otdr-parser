package data;

public class KeyEvents {
    private int number; // номер события
    private double distance; // расстояние до события
    private double attenuationCoefficient; // коэффициент затухания
    private double attenuationInConnection; // Затухание в соединении
    private double reflectionCoefficient; // Коэффициент отражения

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getAttenuationCoefficient() {
        return attenuationCoefficient;
    }

    public void setAttenuationCoefficient(double attenuationCoefficient) {
        this.attenuationCoefficient = attenuationCoefficient;
    }

    public double getAttenuationInConnection() {
        return attenuationInConnection;
    }

    public void setAttenuationInConnection(double attenuationInConnection) {
        this.attenuationInConnection = attenuationInConnection;
    }

    public double getReflectionCoefficient() {
        return reflectionCoefficient;
    }

    public void setReflectionCoefficient(double reflectionCoefficient) {
        this.reflectionCoefficient = reflectionCoefficient;
    }
}
