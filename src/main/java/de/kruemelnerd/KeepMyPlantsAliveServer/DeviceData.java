package de.kruemelnerd.KeepMyPlantsAliveServer;

import java.time.LocalDateTime;

public class DeviceData {
    String device;
    String room;
    int numberInRoom;
    float soilMoisture;
    LocalDateTime dateTime;

    //Just for Jackson
    public DeviceData() {
    }

    public DeviceData(String device, String room, int numberInRoom, float soilMoisture, LocalDateTime dateTime) {
        this.device = device;
        this.room = room;
        this.numberInRoom = numberInRoom;
        this.soilMoisture = soilMoisture;
        this.dateTime = dateTime;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getNumberInRoom() {
        return numberInRoom;
    }

    public void setNumberInRoom(int numberInRoom) {
        this.numberInRoom = numberInRoom;
    }

    public float getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(float soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
