package de.kruemelnerd.KeepMyPlantsAliveServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)

public class DataRepositoryTest {

    @Spy
    ObjectMapper objectMapper;
    @InjectMocks
    DeviceDataRepository deviceDataRepository;

    @BeforeEach
    @AfterEach
    void init() throws IOException {
        this.remove_File_with_data();
    }

    @Test
    void saveDeviceDataTest() throws IOException {
        // given
        DeviceData deviceData = new DeviceData("fancy fox", "arbeitszimmer", 1, 1.1f, LocalDateTime.now());

        // when
        deviceDataRepository.save(deviceData);

        // then
        verify(objectMapper, times(1)).writeValue(any(File.class), anyList());

        assertThat(deviceDataRepository.getAmountOfEntries(), is(1));
    }

    @Test
    void save_multiple_DeviceData_to_file() throws IOException {
        deviceDataRepository.save(new DeviceData("fancy fox", "arbeitszimmer", 1, 1.1f, LocalDateTime.now()));
        deviceDataRepository.save(new DeviceData("sneaky peaky", "küche", 5, 9.1f, LocalDateTime.now()));
        deviceDataRepository.save(new DeviceData("fancy fox", "arbeitszimmer", 1, 2.1f, LocalDateTime.now()));
        deviceDataRepository.save(new DeviceData("sneaky peaky", "küche", 5, 10.1f, LocalDateTime.now()));

        // then
        verify(objectMapper, times(4)).writeValue(any(File.class), anyList());

        assertThat(deviceDataRepository.getAmountOfEntries(), is(4));
    }

    @Test
    void get_multiple_data_from_file() throws IOException {

        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 05, 10, 20, 17, 30);
        DeviceData deviceData0 = new DeviceData("sneaky peaky", "küche", 5, 10.1f, expectedDateTime);
        DeviceData deviceData1 = new DeviceData("fancy fox", "arbeitszimmer", 1, 1.1f, expectedDateTime);
        DeviceData deviceData2 = new DeviceData("sneaky peaky", "küche", 5, 9.1f, expectedDateTime);
        DeviceData deviceData3 = new DeviceData("fancy fox", "arbeitszimmer", 1, 1.1f, expectedDateTime);

        deviceDataRepository.save(deviceData0);
        deviceDataRepository.save(deviceData1);
        deviceDataRepository.save(deviceData2);
        deviceDataRepository.save(deviceData3);

        verify(objectMapper, times(4)).writeValue(any(File.class), anyList());

        List<DeviceData> allEntries = deviceDataRepository.getEntries();

        assertThat(allEntries, hasSize(4));
        assertThat(allEntries.get(0).getDevice(), is(deviceData0.getDevice()));
        assertThat(allEntries.get(0).getRoom(), is(deviceData0.getRoom()));
        assertThat(allEntries.get(0).getNumberInRoom(), is(deviceData0.getNumberInRoom()));
        assertThat(allEntries.get(0).getSoilMoisture(), is(deviceData0.getSoilMoisture()));
        assertThat(allEntries.get(0).getDateTime().toString(), is(expectedDateTime.toString()));

        assertThat(allEntries.get(3).getDevice(), is(deviceData3.getDevice()));
        assertThat(allEntries.get(3).getRoom(), is(deviceData3.getRoom()));
        assertThat(allEntries.get(3).getNumberInRoom(), is(deviceData3.getNumberInRoom()));
        assertThat(allEntries.get(3).getSoilMoisture(), is(deviceData3.getSoilMoisture()));
        assertThat(allEntries.get(3).getDateTime().toString(), is(expectedDateTime.toString()));
    }

    @Test
    void get_data_from_file() throws IOException {
        DeviceData deviceData = new DeviceData("fancy fox", "arbeitszimmer", 1, 1.1f, LocalDateTime.now());
        deviceDataRepository.save(deviceData);
        verify(objectMapper, times(1)).writeValue(any(File.class), anyList());

        List<DeviceData> allEntries = deviceDataRepository.getEntries();

        assertThat(allEntries, hasSize(1));
        assertThat(allEntries.get(0).getDevice(), is(deviceData.getDevice()));
        assertThat(allEntries.get(0).getRoom(), is(deviceData.getRoom()));
        assertThat(allEntries.get(0).getNumberInRoom(), is(deviceData.getNumberInRoom()));
        assertThat(allEntries.get(0).getSoilMoisture(), is(deviceData.getSoilMoisture()));
        assertThat(allEntries.get(0).getDateTime(), is(anything()));

    }


    @Test
    void get_empty_list_from_empty_file() throws IOException {
        List<DeviceData> allEntries = deviceDataRepository.getEntries();
        assertThat(allEntries, hasSize(0));
    }

    @Test
    void remove_File_with_data() throws IOException {
        deviceDataRepository.deleteEverything();

        assertThat(deviceDataRepository.getAmountOfEntries(), is(0));
    }
}
