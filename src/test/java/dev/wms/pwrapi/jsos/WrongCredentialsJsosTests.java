package dev.wms.pwrapi.jsos;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Disabled("This test cases require good response timings from webiste host")
public class WrongCredentialsJsosTests {

    @Test
    public void rootEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void zajeciaEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/zajecia?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void zajeciaTydzienEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/zajecia/tydzien?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void zajeciaTydzienNastepnyEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/zajecia/tydzien/nastepny?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void zajeciaJutroEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/zajecia/jutro?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void zajeciaDzisEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/zajecia/dzis?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void wiadomosciEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/wiadomosci/1?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void wiadomosciPageTrescEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/wiadomosci/1/tresc?login=thisIsNotRealLogin&password=thisIsNotRealPassword&ids=1").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void finanseEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/finanse?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void finanseOperacjeEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/finanse/operacje?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

    @Test
    public void daneEndpointShouldGive401OnWrongCredentials(){

        get("api/jsos/dane?login=thisIsNotRealLogin&password=thisIsNotRealPassword").then()
                .assertThat()
                .statusCode(401);

    }

}
