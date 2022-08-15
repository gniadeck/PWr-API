package dev.wms.pwrapi.eportal;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Disabled("This test cases require good response timings from webiste host")
public class WrongCredentialsEportalTests {

    @Test
    public void rootEndpointShouldGive401OnWrongCredentials(){

        get("api/eportal?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void kursyEndpointShouldGive401OnWrongCredentials(){

        get("api/eportal/kursy?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void kursyIdOcenyEndpointShouldGive401OnWrongCredentials(){

        get("api/eportal/kursy/1/oceny?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void kalendarzEndpointShouldGive401OnWrongCredentials(){

        get("api/eportal/kalendarz?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void kalendarzPobierzEndpointShouldGive401OnWrongCredentials(){

        get("api/eportal/kalendarz/pobierz?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

}
