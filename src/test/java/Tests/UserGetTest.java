package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;

import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Get user data depending on permissions cases")
@Feature("Get user data")
public class UserGetTest extends BaseTestCase {
  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

  @Test
  @Description("This test verifies that unauthorized user does not have access to existing users PII")
  @DisplayName("Receiving a user PII by unauthorized user")
  public void testGetUserDataNotAuth() {
    Response responseUserData = apiCoreRequests
            .makeGetRequestWoAuthData("https://playground.learnqa.ru/api/user/2");

    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasNotField(responseUserData, "firstName");
    Assertions.assertJsonHasNotField(responseUserData, "lastName");
    Assertions.assertJsonHasNotField(responseUserData, "email");
  }

  @Test
  @Description("This test verifies that authorized user has access to his own PII")
  @DisplayName("Receiving own PII data by authorized user")
  public void testGetUserDetailsAuthAsSameUser() {
    Map<String, String> authData = DataGenerator.getRegisteredUserData();

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    String header = this.getHeader(responseGetAuth, "x-csrf-token");
    String cookie = this.getCookie(responseGetAuth, "auth_sid");

    Response responseUserData = apiCoreRequests
            .makeGetRequest("https://playground.learnqa.ru/api/user/2", header, cookie);

    String[] expectedFields = {"username", "firstName", "lastName", "email"};
    Assertions.assertJsonHasFields(responseUserData, expectedFields);
  }

  @Test
  @Description("This test verifies that authorized user does not have access to other users PII")
  @DisplayName("Receiving othe user PII data by authorized user")
  public void testGetUserDetailsAuthAsDifferentUser() {
    Map<String, String> authData = DataGenerator.getRegisteredUserData();

    Response responseGetAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    String header = this.getHeader(responseGetAuth, "x-csrf-token");
    String cookie = this.getCookie(responseGetAuth, "auth_sid");

    Response responseUserData = apiCoreRequests
            .makeGetRequest("https://playground.learnqa.ru/api/user/1", header, cookie);

    String[] expectedFields = {"username", "firstName", "lastName", "email"};
    Assertions.assertJsonHasField(responseUserData, "username");
    Assertions.assertJsonHasNotField(responseUserData, "firstName");
    Assertions.assertJsonHasNotField(responseUserData, "lastName");
    Assertions.assertJsonHasNotField(responseUserData, "email");
  }
}
