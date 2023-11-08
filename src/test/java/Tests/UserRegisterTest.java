package Tests;

import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import lib.ApiCoreRequests;

import java.util.Map;
import java.util.HashMap;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

  private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
  @Test
  @Description("This test verifies user registration impossibility with existing email")
  @DisplayName("Test negative registration with existing email")
  public void testCreateUserWithExistingEmail(){
    String email = "vinkotov@example.com";

    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData.put("password", "1234");
    userData.put("username", "learnqa");
    userData.put("firstName", "learnqa");
    userData.put("lastName", "learnqa");

    Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
  }
  @Test
  @Description("This test verifies successful user registration possibility")
  @DisplayName("Test positive registration")
  public void testCreateUserSuccessfully(){
    String email = DataGenerator.getRandomEmail();

    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData.put("password", "1234");
    userData.put("username", "learnqa");
    userData.put("firstName", "learnqa");
    userData.put("lastName", "learnqa");

    Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

    Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
    Assertions.assertJsonHasField(responseCreateAuth, "id");
  }
  @Test
  @Description("This test verifies user registration impossibility with invalid email")
  @DisplayName("Test negative registration with invalid email")
  public void testCreateUserWithIncorrectEmail() {
    String email = "vinkotovexample.com";

    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData.put("password", "123");
    userData.put("username", "learnqa");
    userData.put("firstName", "learnqa");
    userData.put("lastName", "learnqa");

    Response responseCreateAuth = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
  }

  @Description("This test verifies user registration impossibility without mandatory field filling")
  @DisplayName("Test negative registration without mandatory field")
  @ParameterizedTest
  @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
  public void testCreateUserWithoutMandatoryField(String field) {
    Map<String, String> userData = DataGenerator.getRegistrationData();
    userData.remove(field);

    Response responseCreateAuthWoField = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

    Assertions.assertResponseCodeEquals(responseCreateAuthWoField, 400);
    Assertions.assertResponseTextEquals(responseCreateAuthWoField, String.format("The following required params are missed: %s", field));
  }
  @Test
  @Description("This test verifies user registration impossibility with short name")
  @DisplayName("Test negative registration with short name")
  public void testCreateUserWithShortName() {
    Map<String, String> userData = DataGenerator.getRegistrationData();
    userData.replace( "firstName", "learnqa", "u");

    Response responseCreateUserWithShortName = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

    Assertions.assertResponseCodeEquals(responseCreateUserWithShortName, 400);
    Assertions.assertResponseTextEquals(responseCreateUserWithShortName, "The value of 'firstName' field is too short");
  }
  @Test
  @Description("This test verifies user registration impossibility with long name")
  @DisplayName("Test negative registration with long name")
  public void testCreateUserWithLongName() {
    Map<String, String> userData = DataGenerator.getRegistrationData();
    String longFirstName = "firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols11";
    userData.replace( "firstName", longFirstName);

    Response responseCreateUserWithLongName = apiCoreRequests
            .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

    Assertions.assertResponseCodeEquals(responseCreateUserWithLongName, 400);
    Assertions.assertResponseTextEquals(responseCreateUserWithLongName, "The value of 'firstName' field is too long");
  }
}