package DesignPatterns;

import java.util.ArrayList;
import java.util.List;

interface IObserver {
    void update(float temperature);
}

class WeatherStation {
    private final List<IObserver> observers = new ArrayList<>();
    private float temperature;

    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
        notifyObservers();
    }

    private void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update(temperature);
        }
    }
}

class TemperatureDisplay implements IObserver {
    public void update(float temperature) {
        System.out.println("Temperature is now: " + temperature);
    }
}

public class Observer {
    public static void main(String[] args) {
        WeatherStation station = new WeatherStation();
        TemperatureDisplay display = new TemperatureDisplay();
        station.addObserver(display);
        station.setTemperature(25.0f); // Outputs: Temperature is now: 25.0
    }
}
