package model;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("SpellCheckingInspection")
public class WeatherRepositoryImpl implements WeatherRepository {
    private static final String WEATHER_API = "API_KEY"; //https://openweathermap.org/api

    @Override
    public synchronized Weather getCurrentWeather(Location location) throws IOException {
        Float longitude = location.getLongitude();
        Float latitude = location.getLatitude();

        URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&APPID=%s",
                latitude.toString(), longitude.toString(), WEATHER_API));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        return createWeatherFromResponse(getResponce(connection));
    }

    private String getResponce(HttpURLConnection connection) throws IOException {
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseStringBuilder = new StringBuilder();

        while (responseReader.ready())
            responseStringBuilder.append(responseReader.readLine());
        responseReader.close();

        return responseStringBuilder.toString();
    }

    private Weather createWeatherFromResponse(String response) {
        JSONObject jsonObject = new JSONObject(response);

        double temperatureInKelvins = jsonObject.getJSONObject("main").getDouble("temp");
        int humidity = jsonObject.getJSONObject("main").getInt("humidity");
        int pressure = jsonObject.getJSONObject("main").getInt("pressure");

        String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
        String weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

        double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
        double windDegrees = jsonObject.getJSONObject("wind").getDouble("deg");

        return new Weather(temperatureInKelvins, humidity, pressure, weather, weatherDescription, windSpeed, windDegrees);
    }
}
