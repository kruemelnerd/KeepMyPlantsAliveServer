package de.kruemelnerd.KeepMyPlantsAliveServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestControllerSoilMoisture {

    Logger logger = LogManager.getLogger(this.getClass());

    private final List<DeviceData> deviceDataList = new ArrayList<>();

    DeviceDataRepository repository;

    @Autowired
    public RestControllerSoilMoisture(DeviceDataRepository repository) {
        this.repository = repository;
    }


    @Autowired
    private TelegramBotService telegramBotService;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        telegramBotService.sendMessage(message);
        return "Message sent";
    }

    @PostMapping(value = "saveSoilMoisture")
    public ResponseEntity saveDeviceData(@RequestBody DeviceData data) {
        try {
            logger.info("new Data received: " + data.toString());
            if (data.getSoilMoisture() < 0) {
                data.setSoilMoisture(0);
            }
            if (data.getSoilMoisture() < 30) {
                //telegramBotService.sendCriticalSoilMoistureStatus(data);
            }
            repository.save(data);
        } catch (IOException e) {
            e.printStackTrace(); // see note 2
            return ResponseEntity.badRequest().body("Error while handeling the file.");
        }
        return new ResponseEntity<DeviceData>(data, HttpStatus.CREATED);
    }

    @GetMapping("/data")
    ResponseEntity getAllData() {
        try {
            logger.info("getAllData!");
            return new ResponseEntity<>(repository.getEntries(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace(); // see note 2
            return ResponseEntity.badRequest().body("Error while handeling the file.");
        }
    }

    @PostMapping("/deleteEverything")
    ResponseEntity deleteList() {

        try {
            logger.info("EVERYTHING IS GONE!");
            repository.deleteEverything();
            return new ResponseEntity(HttpStatus.RESET_CONTENT);
        } catch (Exception e) {
            e.printStackTrace(); // see note 2
            return ResponseEntity.badRequest().body("Error while handeling the file.");
        }
    }

    @GetMapping("/amountOfEntries")
    ResponseEntity getAmountOfEntries() {
        try {

            return ResponseEntity.ok(Collections.singletonMap("amount", repository.getAmountOfEntries()));
        } catch (IOException e) {
            e.printStackTrace(); // see note 2
            return ResponseEntity.badRequest().body("Error while handeling the file.");
        }
    }

    @PostMapping("/testdata")
    ResponseEntity generateTestData(@RequestParam int amount) {
        for (int i = 0; i < amount; i++) {
            int min = 0;
            int max = 100;
            double random = min + Math.random() * (max - min);
            DeviceData deviceData = new DeviceData("Floating Fox", "arbeitszimmer", 1, (float) random, LocalDateTime.now());
            this.saveDeviceData(deviceData);

        }
        return ResponseEntity.ok(amount);
    }

}
