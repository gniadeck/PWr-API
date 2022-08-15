package dev.wms.pwrapi.parking;

import dev.wms.pwrapi.api.ParkingAPI;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ParkingTests {

    @Test
    public void parkingEndpointShouldReturnValueMatchingJsonSchema(){

        get("/api/parking").then().assertThat()
                .body(matchesJsonSchemaInClasspath("parking/parking-schema.json"))
                .statusCode(200);

    }

    @Test
    public void parkingLeftPlacesShouldContainOnlyPositiveNumbers(){

        get("api/parking").then()
                .body("[0].leftPlaces", greaterThanOrEqualTo(0))
                .body("[1].leftPlaces", greaterThanOrEqualTo(0))
                .body("[2].leftPlaces", greaterThanOrEqualTo(0))
                .body("[3].leftPlaces", greaterThanOrEqualTo(0))
                .body("[4].leftPlaces", greaterThanOrEqualTo(0));

    }

    @Test
    public void allParkingNamesShouldBeAccessible(){

        get("api/parking").then()
                .body("[0].name", oneOf("D20", "Parking Wrońskiego", "C13", "Geocentrum", "Architektura"))
                .body("[1].name", oneOf("D20", "Parking Wrońskiego", "C13", "Geocentrum", "Architektura"))
                .body("[2].name", oneOf("D20", "Parking Wrońskiego", "C13", "Geocentrum", "Architektura"))
                .body("[3].name", oneOf("D20", "Parking Wrońskiego", "C13", "Geocentrum", "Architektura"))
                .body("[4].name", oneOf("D20", "Parking Wrońskiego", "C13", "Geocentrum", "Architektura"));

    }

    @Test
    public void responseTypeShouldBeJSON(){

        get("api/parking").then()
                .contentType(ContentType.JSON);

    }



}
