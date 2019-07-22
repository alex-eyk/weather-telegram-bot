package com.telegram.bot.weather.model;

import org.telegram.telegrambots.meta.api.objects.Location;

import java.io.IOException;

public interface WeatherRepository {
    Weather getCurrentWeatherByLocation(Location location) throws IOException;
    Weather getCurrentWeatherByZipCode(String zipCode) throws IOException;
}
