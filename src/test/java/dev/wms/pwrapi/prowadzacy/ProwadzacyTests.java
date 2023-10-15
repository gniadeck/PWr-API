package dev.wms.pwrapi.prowadzacy;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Disabled
public class ProwadzacyTests {

    @Test
    public void szukajEndpointFailOnBadQuery(){

        get("api/prowadzacy/szukaj?query=omgIamSuchABadQuery&offset=0").then()
                .assertThat()
                .statusCode(500);


    }

    @Test
    public void szukajEndpointBadRequestOnHugeOffset(){

        get("api/prowadzacy/szukaj?query=Dariusz Konieczny&offset=10000")
                .then().assertThat()
                .statusCode(500);


    }

    @Test
    public void szukajEndpointGoodTitleOnGoodQuery(){
        //I hope dariusz konieczny is still working in politechnika...

        get("api/prowadzacy/szukaj?query=Dariusz Konieczny")
                .then()
                .assertThat()
                .body("title", equalTo("Dariusz Konieczny"));

    }

    @Test
    public void szukajEndpointShouldWorkOnNoOffset(){

        get("api/prowadzacy/szukaj?query=omgIamSuchABadQuery")
                .then()
                .assertThat()
                .statusCode(500);


    }

    @Test
    public void szukajEndpointShouldWorkWithOffset(){
        get("api/prowadzacy/szukaj?query=Dariusz Konieczny&offset=-5")
                .then()
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void salaEndpointShouldNotWorkWhenOnlyBuildingProvided(){
        get("api/prowadzacy/szukaj/sala?building=D-20")
                .then()
                .assertThat()
                .statusCode(500);

    }

    @Test
    public void salaEndpointShouldNotWorkWhenOnlyRoomProvided(){

        get("api/prowadzacy/szukaj/sala?room=311-d")
                .then()
                .assertThat()
                .statusCode(500);

    }

    @Test
    public void salaEndpointShouldFailWhenBadQueryReceived(){

        get("api/prowadzacy/szukaj/sala?building=omgImSuchABadBuilding&room=omgImSuchABadRoom")
                .then()
                .assertThat()
                .statusCode(500);


    }


    @Test
    public void szukajPrzedmiotEndpointReturnsEmptyOnBadQuery(){

        get("api/prowadzacy/szukaj/przedmiot?query=omgIamSuchABadQuery").then()
                .assertThat()
                .statusCode(500);


    }






}
