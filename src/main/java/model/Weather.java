package model;

public class Weather {
    private double temperature;
    private int humidity;
    private int pressure;
    private String weather;
    private String weatherDescription;
    private double windSpeed;
    private double windDegrees;

    Weather(double temperatureInKelvins, int humidity, int pressure, String weather, String weatherDescription,
            double windSpeed, double windDegrees) {
        this.temperature = temperatureInKelvins - 273.15;
        this.humidity = humidity;
        this.pressure = (int) ((pressure * 100) / 133.322);
        this.weather = weather;
        this.weatherDescription = weatherDescription;
        this.windSpeed = windSpeed;
        this.windDegrees = windDegrees;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public String getWeather() {
        return weather;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getWindDegrees() {
        return windDegrees;
    }
}
