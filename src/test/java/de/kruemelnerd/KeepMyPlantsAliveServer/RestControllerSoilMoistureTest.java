package de.kruemelnerd.KeepMyPlantsAliveServer;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RestControllerSoilMoistureTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    @AfterEach
    void setup() {
        check_cleanup_is_working();
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
    public void test_Input() {
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 7.1f);

        given()
                .port(port)
                .when()
                .get("/api/amountOfEntries")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", is(1));

    }

    @Test
    public void send_multiple_entries_to_controller() {
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 1.1f);
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 2.1f);
        sendEntryToController("Professor Clean", "Küche", 1, 3.1f);
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 4.1f);

        given()
                .port(port)
                .when()
                .get("/api/amountOfEntries")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", is(4));

    }

    private void sendEntryToController(String device, String room, int numberInRoom, float soilMoisture) {
        String inputJson = "{ \"device\": \"" + device + "\", \"room\": \"" + room + "\", \"numberInRoom\": " + numberInRoom
                + ", \"soilMoisture\": " + soilMoisture + " }";


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
                .body("soilMoisture", is(soilMoisture));
    }

    @Test
    public void testInputAndStorage() {
        sendEntryToController("fancy fox", "arbeitszimmer", 1, 5.1f);
        sendEntryToController("sneaky peaky", "arbeitszimmer",4 , 1f);

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
                .body("[0].soilMoisture", is(5.1F))

                .body("[1].device", is("sneaky peaky"))
                .body("[1].room", is("arbeitszimmer"))
                .body("[1].numberInRoom", is(4))
                .body("[1].soilMoisture", is(1F));
    }


}
