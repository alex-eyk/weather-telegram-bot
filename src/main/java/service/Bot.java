package service;

import model.Weather;
import model.WeatherRepository;
import model.WeatherRepositoryImpl;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    private static final String BOT_NAME = "WeatherBot";
    private static final String TELEGRAM_API = "API_KEY"; //Ключ можно получить у телеграм-бота @BotFather
    private WeatherRepository mWeatherRepository = new WeatherRepositoryImpl();

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        if (message.getText() != null) {
            if (message.getText().equals("/start"))
                sendMessage(chatId, Messages.GREETING);
            else if (message.getText().equals("/help"))
                sendMessage(chatId, Messages.HELP);
            else sendMessage(chatId, Messages.UNKNOWN);
        } else if (message.getLocation() != null) {
            try {
                Weather currentWeather = mWeatherRepository.getCurrentWeather(message.getLocation());
                sendMessage(chatId, MessagesBuilderHelper.createMessage(currentWeather));
            } catch (IOException e) {
                sendMessage(chatId, Messages.ERROR);
            }
        } else sendMessage(chatId, Messages.ERROR);
    }

    private synchronized void sendMessage(Long chatId, String message) {
        SendMessage messageSender = new SendMessage()
                .enableMarkdown(true)
                .setChatId(chatId)
                .setText(message)
                .setReplyMarkup(new ReplyKeyboardMarkup().setKeyboard(new ArrayList<KeyboardRow>() {{
                    KeyboardRow keyboardRow = new KeyboardRow();
                    keyboardRow.add(new KeyboardButton("/help"));
                    add(keyboardRow);
                }}));

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
