package service;

import model.Weather;

import java.math.BigDecimal;
import java.math.RoundingMode;

class MessagesBuilderHelper {
    static String createMessage(Weather currentWeather) {
        StringBuilder builder = new StringBuilder()
                .append("Привет! Текущая температура - ")
                .append(new BigDecimal(currentWeather.getTemperature()).setScale(1, RoundingMode.HALF_UP).doubleValue())
                .append("°, ");

        switch (currentWeather.getWeather()) {
            case "Clear":
                builder.append("небо чистое");
                break;
            case "Clouds":
                if (currentWeather.getWeatherDescription().contains("broken")) builder.append("облачно");
                else builder.append("малооблачно");
                break;
            case "Rain":
                if (currentWeather.getWeatherDescription().contains("light")) builder.append("идет легкий дождь");
                else builder.append("идет дождь");
                break;
            case "Snow":
                builder.append("идет снег");
                break;
        }
        builder.append(", ");

        builder.append("влажность - ")
                .append(currentWeather.getHumidity())
                .append("%, ");

        builder.append("ветер ")
                .append(getDirection(currentWeather.getWindDegrees()))
                .append(", ")
                .append(getWindySpeed(currentWeather.getWindSpeed()))
                .append(", ");

        builder.append("давление - ")
                .append(getPressure(currentWeather.getPressure()))
                .append(".");

        builder.append(" Удачного дня!");
        return builder.toString();
    }

    private static String getDirection(double windyAngle) {
        int angle = (int) windyAngle;
        if (angle >= 330 || angle <= 30)
            return "северный";
        if (angle < 60)
            return "северо-восточный";
        if (angle <= 120)
            return "восточный";
        if (angle < 150)
            return "юго-восточный";
        if (angle <= 210)
            return "южный";
        if (angle < 240)
            return "юго-западный";
        if (angle <= 300)
            return "западный";
        return "северо-западный";
    }

    private static String getWindySpeed(double windySpeed) {
        String stringWindySpeed;
        if (windySpeed % 1 < 0.1)
            stringWindySpeed = String.valueOf((int) windySpeed);
        else stringWindySpeed = new BigDecimal(windySpeed).setScale(1, RoundingMode.HALF_UP).toString();

        if (windySpeed <= 20) {
            if (windySpeed >= 1 && windySpeed < 5) return stringWindySpeed + " метра в секунду";
            if (windySpeed == 1) return stringWindySpeed + " метр в секунду";
        } else {
            double remainder = windySpeed % 10;
            if (remainder == 1) return stringWindySpeed + " метр в секунду";
            if (remainder > 1 && remainder < 5) return stringWindySpeed + " метра в секунду";
        }
        return stringWindySpeed + " метров в секунду";
    }

    private static String getPressure(int pressure) {
        int remainder = pressure % 10;
        if (remainder > 4) return pressure + " миллиметров ртутного столба";
        if (remainder == 1) return pressure + " миллиметр ртутного столба";
        return pressure + " миллиметра ртутного столба";
    }
}