package de.kruemelnerd.KeepMyPlantsAliveServer;

public class DeviceData {
    String device;
    String room;
    int numberInRoom;
    float soilMoisture;

    //Just for Jackson
    public DeviceData() {
    }

    public DeviceData(String device, String room, int numberInRoom, float soilMoisture) {
        this.device = device;
        this.room = room;
        this.numberInRoom = numberInRoom;
        this.soilMoisture = soilMoisture;
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
}
