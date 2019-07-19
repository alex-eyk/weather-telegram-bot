package model;

import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.IOException;

public interface WeatherRepository {
    Weather getCurrentWeather(Location location) throws IOException;
}
