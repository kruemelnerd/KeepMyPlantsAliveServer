package de.kruemelnerd.KeepMyPlantsAliveServer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.channel.id}")
    private String channelID;


    private TelegramBot bot;

    public TelegramBotService(@Value("${telegram.bot.token}") String botToken) {
        this.botToken = botToken;
        this.bot = new TelegramBot(botToken);
    }


    public void sendMessage(String message) {
        SendMessage request = new SendMessage(channelID, message);
        SendResponse response = bot.execute(request);
    }

    public void sendCriticalSoilMoistureStatus(DeviceData data) {
        String message = "For the device " + data.getDevice() +
                " the Soil Moisture Level is at " + data.getSoilMoisture() + "%. Please check Room " + data.getRoom() +
                " and Number " + data.getNumberInRoom() + " as soon as possible.";

        sendMessage(message);
    }
}
