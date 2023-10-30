import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookieValue {
  @Test
  public void testAuthUser() {
    Response responseCookies = RestAssured
            .given()
            .get("https://playground.learnqa.ru/api/homework_cookie")
            .andReturn();

    String value = responseCookies.getCookie("Homework");
    Map<String, String> cookies = responseCookies.getCookies();
    assertTrue(cookies.containsKey("HomeWork"), "Incorrect cookie key");
    assertTrue(cookies.containsValue("hw_value"), "Incorrect cookie value");
    assertEquals("hw_value", value, "Incorrect cookie value");
    System.out.println(cookies);
  }

}
