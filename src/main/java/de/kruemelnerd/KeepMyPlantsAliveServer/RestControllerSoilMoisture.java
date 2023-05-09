package de.kruemelnerd.KeepMyPlantsAliveServer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RestControllerSoilMoisture {

    private final List<DeviceData> deviceDataList = new ArrayList<>();

    @PostMapping(value = "saveSoilMoisture")
    public ResponseEntity<DeviceData> saveDeviceData(@RequestBody DeviceData data) {
        deviceDataList.add(data);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @GetMapping("/data")
    ResponseEntity<List> getAllData() {
        return new ResponseEntity<>(deviceDataList, HttpStatus.OK);
    }

    @PostMapping("/deleteEverything")
    ResponseEntity deleteList() {
        deviceDataList.removeAll(deviceDataList);
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }

    @GetMapping("/amountOfEntries")
    ResponseEntity<Map> getAmountOfEntries() {
        return ResponseEntity.ok(Collections.singletonMap("amount", deviceDataList.size()));
    }

}
