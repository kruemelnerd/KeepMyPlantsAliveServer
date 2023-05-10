package de.kruemelnerd.KeepMyPlantsAliveServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestControllerSoilMoisture {

    private final List<DeviceData> deviceDataList = new ArrayList<>();

    DeviceDataRepository repository;

    @Autowired
    public RestControllerSoilMoisture(DeviceDataRepository repository) {
        this.repository = repository;
    }


    @PostMapping(value = "saveSoilMoisture")
    public ResponseEntity saveDeviceData(@RequestBody DeviceData data) {
        try {
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
            return new ResponseEntity<>(repository.getEntries(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace(); // see note 2
            return ResponseEntity.badRequest().body("Error while handeling the file.");
        }
    }

    @PostMapping("/deleteEverything")
    ResponseEntity deleteList() {

        try {
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

}
