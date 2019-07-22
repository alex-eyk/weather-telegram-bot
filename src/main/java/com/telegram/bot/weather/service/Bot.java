package com.telegram.bot.weather.service;

import com.telegram.bot.weather.model.Weather;
import com.telegram.bot.weather.model.WeatherRepository;
import com.telegram.bot.weather.model.WeatherRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class Bot extends TelegramLongPollingBot {
    private static final String BOT_NAME = "WeatherBot";
    private static final String TELEGRAM_API = "API_KEY"; //Ключ можно получить у телеграм-бота @BotFather
    private WeatherRepository mWeatherRepository = new WeatherRepositoryImpl();

    private static final Logger logger = LogManager.getLogger(Bot.class);

    @Override
    public synchronized void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        if (message.getText() != null) {
            if (message.getText().equals("/start"))
                sendMessage(chatId, Messages.GREETING);

            else if (message.getText().equals("/help"))
                sendMessage(chatId, Messages.HELP);

            else if (message.getText().matches("\\d+"))
                new Thread(() -> sendWeatherWithZipCode(chatId, message)).start();

            else sendMessage(chatId, Messages.UNKNOWN);

        } else if (message.getLocation() != null)
            new Thread(() -> sendWeatherWithLocation(chatId, message)).start();
    }

    private void sendWeatherWithLocation(Long chatId, Message message) {
        try {
            Weather currentWeather = mWeatherRepository.getCurrentWeatherByLocation(message.getLocation());
            sendMessage(chatId, MessagesBuilderHelper.createMessage(currentWeather));
        } catch (IOException e) {
            sendMessage(chatId, Messages.ERROR);
            logger.error("Exception while getting current weather with location", e);
        }
    }

    private void sendWeatherWithZipCode(Long chatId, Message message) {
        try {
            Weather currentWeather = mWeatherRepository.getCurrentWeatherByZipCode(message.getText());
            sendMessage(chatId, MessagesBuilderHelper.createMessage(currentWeather));
        } catch (IOException e) {
            sendMessage(chatId, Messages.ERROR);
            logger.error("Exception while getting current weather with zip code", e);
        }
    }

    private synchronized void sendMessage(Long chatId, String message) {
        SendMessage messageSender = new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatId)
                .setText(message);

        try {
            execute(messageSender);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return TELEGRAM_API;
    }
}
