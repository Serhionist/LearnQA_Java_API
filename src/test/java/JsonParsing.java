import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class JsonParsing {

  @Test
  public void testJsonParsing() {

    JsonPath responseWithToken = RestAssured
            .get("https://playground.learnqa.ru/api/get_json_homework")
            .jsonPath();

    String message = responseWithToken.getString("messages[1].message");

    System.out.println(message);

  }
}