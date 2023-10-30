import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Header {
  @Test
  public void testHeader () {
    Response responseHeader = RestAssured
            .given()
            .get("https://playground.learnqa.ru/api/homework_header")
            .andReturn();

    //responseHeader.prettyPrint();

    Headers headers = responseHeader.getHeaders();

    assertTrue(headers.hasHeaderWithName("X-Secret-Homework-Header"), "Incorrect header");
    }
}
