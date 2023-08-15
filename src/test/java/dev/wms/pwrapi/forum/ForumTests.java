package dev.wms.pwrapi.forum;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ForumTests {

    /*
    @MockBean
    ForumService forumService;

    @Test
    public void metadataEndpointTest(){
        get("api/forum").then()
                .assertThat()
                .contentType("application/json")
                .statusCode(200);
    }

    @Test
    public void fetchTeacherByIdEndpointTest() throws JsonProcessingException {
        Teacher teacher = new Teacher(1, "matematycy", "Prof. dr hab.", "Wojciech Suszko", 5.36);

        get("api/forum/prowadzacy/1").then()
                .assertThat()
                .contentType("application/json")
                .statusCode(200)
                .body("id", equalTo(1))
                .body("category", equalTo("matematycy"))
                .body("fullName", equalTo("Adam Abrams"));
    }

     */
}
