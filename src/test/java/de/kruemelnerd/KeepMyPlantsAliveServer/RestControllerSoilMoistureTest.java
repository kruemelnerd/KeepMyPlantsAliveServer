package de.kruemelnerd.KeepMyPlantsAliveServer;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)

public class RestControllerSoilMoistureTest {

    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    @LocalServerPort
    private int port;

    @MockBean
    TelegramBotService telegramBotService;

    @MockBean
    FileRepository repository;


    RestControllerSoilMoisture controllerSoilMoisture;


    @BeforeEach
    @AfterEach
    void setup() {
        //telegramBotService = new TelegramBotService("123");
        //RestAssuredMockMvc.mockMvc(mockMvc);

        RestControllerSoilMoisture spyControllerSoilMoisture = new RestControllerSoilMoisture(repository, telegramBotService);
        controllerSoilMoisture = Mockito.spy(spyControllerSoilMoisture);
        //controllerSoilMoisture.deleteList();
    }

    @Test
    void check_cleanup_is_working() {
        //test_Input();
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .post("/api/deleteEverything")
                .then()
                .statusCode(HttpStatus.RESET_CONTENT.value());

        given()
                .port(port)
                .when()
                .get("/api/amountOfEntries")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", is(0));

    }

    @Test
    public void test_one_single_Input() throws IOException {
        when(this.repository.getAmountOfEntries()).thenReturn(1);

        sendEntryToController("fancy fox", "arbeitszimmer", 1, 72.1f);

        given()
                .port(port)
                .when()
                .get("/api/amountOfEntries")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", is(1));

        verify(this.repository, times(1)).save(any());
        verify(this.repository, times(1)).getAmountOfEntries();

    }

    @Test
    public void send_multiple_entries_to_controller() throws IOException {
        when(this.repository.getAmountOfEntries()).thenReturn(4);
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 61.1f);
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 52.1f);
        sendEntryToController("Professor Clean", "Küche", 1, 93.1f);
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 74.1f);

        given()
                .port(port)
                .when()
                .get("/api/amountOfEntries")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", is(4));

    }

    private void sendEntryToController(String device, String room, int numberInRoom, float soilMoisture) {
        //LocalDateTime expected = LocalDateTime.of(2023, 05, 10, 16, 06, 9);
        LocalDateTime dateTime = LocalDateTime.now();
        String expectedDateTime = dateTime.format(CUSTOM_FORMATTER);


        String inputJson = "{ \"device\": \"" + device + "\", \"room\": \"" + room + "\", \"numberInRoom\": " + numberInRoom
                + ", \"soilMoisture\": " + soilMoisture + ", \"dateTime\": \"" + expectedDateTime + "\" }";
        // example: { "device": "fancy fox", "room": "arbeitszimmer", "numberInRoom": 1, "soilMoisture": 5.1, "dateTime": "2023-05-10T16:06:09" }

        if (soilMoisture < 0f) {
            soilMoisture = 0f;
        }
        // Teste den POST-Request zum Speichern des Inputs
        given()
                .contentType("application/json")
                .body(inputJson)
                .port(port)
                .when()
                .post("/api/saveSoilMoisture")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("device", is(device))
                .body("room", is(room))
                .body("numberInRoom", is(numberInRoom))
                .body("soilMoisture", is(soilMoisture))
                .body("dateTime", is(expectedDateTime));
    }

    @Test
    public void testInputAndStorage() throws IOException {

        List<DeviceData> expectedList = new ArrayList<>();
        expectedList.add(new DeviceData("fancy fox", "arbeitszimmer", 1, 75.1f, LocalDateTime.now()));
        expectedList.add(new DeviceData("sneaky peaky", "arbeitszimmer", 4, 71f, LocalDateTime.now()));
        when(this.repository.getEntries()).thenReturn(expectedList);

        sendEntryToController("fancy fox", "arbeitszimmer", 1, 75.1f);
        sendEntryToController("sneaky peaky", "arbeitszimmer", 4, 71f);

        // Teste den GET-Request zum Abrufen des gespeicherten Inputs
        given()
                .port(port)
                .accept("application/json")
                .when()
                .get("/api/data")
                .then()
                .statusCode(200)
                .body("[0].device", is("fancy fox"))
                .body("[0].room", is("arbeitszimmer"))
                .body("[0].numberInRoom", is(1))
                .body("[0].soilMoisture", is(75.1F))

                .body("[1].device", is("sneaky peaky"))
                .body("[1].room", is("arbeitszimmer"))
                .body("[1].numberInRoom", is(4))
                .body("[1].soilMoisture", is(71F));
    }

    @Test
    public void send_negativ_SoilMoisture_to_controller() throws IOException {

        when(this.repository.getAmountOfEntries()).thenReturn(1);

        sendEntryToController("fancy fox", "arbeitszimmer", 1, -7f);

        given()
                .port(port)
                .when()
                .get("/api/amountOfEntries")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", is(1));

    }

    @Test
    void check_if_telegram_bot_is_triggered_when_SoilMoisture_really_low() {

        String device = "fancy fox";
        String room = "arbeitszimmer";
        int numberInRoom = 1;
        float soilMoisture = 20f;


        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDate = dateTime.format(CUSTOM_FORMATTER);


        String inputJson = "{ \"device\": \"" + device + "\", \"room\": \"" + room + "\", \"numberInRoom\": " + numberInRoom
                + ", \"soilMoisture\": " + soilMoisture + ", \"dateTime\": \"" + formattedDate + "\" }";

        // Teste den POST-Request zum Speichern des Inputs
        given()
                .contentType("application/json")
                .body(inputJson)
                .port(port)
                .when()
                .post("/api/saveSoilMoisture")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("device", is(device))
                .body("room", is(room))
                .body("numberInRoom", is(numberInRoom))
                .body("soilMoisture", is(soilMoisture))
                .body("dateTime", is(formattedDate));

        verify(this.telegramBotService, times(1)).sendCriticalSoilMoistureStatus(any(DeviceData.class));

    }


}
