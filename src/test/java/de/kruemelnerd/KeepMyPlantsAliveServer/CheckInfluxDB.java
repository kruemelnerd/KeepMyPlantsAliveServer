package de.kruemelnerd.KeepMyPlantsAliveServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckInfluxDB {


    String influxUrl;
    String influxUser;
    String influxPassword;

    Logger logger = LogManager.getLogger(this.getClass());
    InfluxDB influxDB;

    public CheckInfluxDB(@Value("${influx.url}") String influxUrl, @Value("${influx.user}") String influxUser, @Value("${influx.password}") String influxPassword) {
        this.influxUrl = influxUrl;
        this.influxUser = influxUser;
        this.influxPassword = influxPassword;

        influxDB = InfluxDBFactory.connect(influxUrl, influxUser, influxPassword);
    }

    @Test
    void check_if_database_is_available() {
        Pong response = this.influxDB.ping();
        logger.info("Response: " + response.getVersion());
        assertThat(response.getVersion().equalsIgnoreCase("unknown"), is(false));
    }
}
