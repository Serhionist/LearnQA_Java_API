import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LongRedirect {
  @Test
  public void longRedirect() {

    while (true) {
      Response response = RestAssured
              .given()
              .redirects()
              .follow(false)
              .when()
              .get("https://playground.learnqa.ru/api/long_redirect")
              .andReturn();

      String locationHeader = response.getHeader("Location");
      if (locationHeader == null) {
        break;
      }
      System.out.println(locationHeader);
    }
  }
}
