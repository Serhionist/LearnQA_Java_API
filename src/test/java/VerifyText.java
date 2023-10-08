import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class VerifyText {
  @Test
  public void testHelloFrom(){
    Response response = RestAssured
            .get(path: "https://playground.learnqa.ru/api/get_text")
            .andReturn();
    response.prettyPrint();
  }
}