package de.kruemelnerd.KeepMyPlantsAliveServer;

import java.io.IOException;
import java.util.List;

public interface DeviceDataRepository {

    void save(DeviceData deviceData) throws IOException;

    void deleteEverything();

    int getAmountOfEntries() throws IOException;

    List<DeviceData> getEntries() throws IOException;

}
