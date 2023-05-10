package de.kruemelnerd.KeepMyPlantsAliveServer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeviceDataRepository {

    Logger logger = LogManager.getLogger(this.getClass());

    ObjectMapper objectMapper;


    private static final String FILE_PATH = "./allSoilMoistureDataFile.json";

    public DeviceDataRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

    }

    private List<DeviceData> getDeviceDataList(File file) {
        if (file.length() == 0) {
            return new ArrayList<>();
        }
        try {
            // Lesen der vorhandenen Daten aus der Datei
            return objectMapper.readValue(file, new TypeReference<List<DeviceData>>() {
            });
        } catch (Exception ex) {
            logger.error("Fehler beim Auslesen der Datei", ex);
            return new ArrayList<>();
        }
    }

    private void createFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            logger.info("File " + FILE_PATH + " not found.");
            file.createNewFile();
        }
    }

    public void save(DeviceData deviceData) throws IOException {

        File file = new File(FILE_PATH);
        createFileIfNotExists(file);
        List<DeviceData> deviceDataList = getDeviceDataList(file);
        // Neue Daten hinzufügen
        deviceDataList.add(deviceData);

        // Schreiben der aktualisierten Daten zurück in die Datei
        objectMapper.writeValue(file, deviceDataList);
    }

    public void deleteEverything() {
        File file = new File(FILE_PATH);
        file.delete();
    }

    public int getAmountOfEntries() throws IOException {
        File file = new File(FILE_PATH);
        createFileIfNotExists(file);
        List<DeviceData> deviceDataList = getDeviceDataList(file);
        return deviceDataList.size();
    }

    public List<DeviceData> getEntries() throws IOException {
        File file = new File(FILE_PATH);
        createFileIfNotExists(file);
        return getDeviceDataList(file);
    }
}
