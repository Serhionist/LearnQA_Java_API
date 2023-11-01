import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import java.lang.Thread;

public class Token {
  @Test
  public void token() throws InterruptedException {

    JsonPath createTask = RestAssured
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();

    String getToken = createTask.get("token");
    System.out.println("token value = " + getToken);
    int getTiming = createTask.get("seconds");
    System.out.println(getTiming + " seconds is needed for the job completion");

   JsonPath beforeTaskCompletion = RestAssured
            .given()
            .queryParam("token", getToken)
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();

        String getStatus = beforeTaskCompletion.get("status");
        System.out.println("Current status is: " + getStatus);

    Thread.sleep(getTiming * 1000L);

    JsonPath afterTaskCompletion = RestAssured
            .given()
            .queryParam("token", getToken)
            .get("https://playground.learnqa.ru/ajax/api/longtime_job")
            .jsonPath();

    String getNewStatus = afterTaskCompletion.get("status");
    String result = afterTaskCompletion.get("result");
    System.out.println("Status after the task completion is: " + getNewStatus);
    System.out.println("Result: " + result);
  }
}
