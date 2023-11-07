package Tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.HashMap;

public class UserRegisterTest extends BaseTestCase {
  @Test
  public void testCreateUserWithExistingEmail(){
    String email = "vinkotov@example.com";

    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData.put("password", "123");
    userData.put("username", "learnqa");
    userData.put("firstName", "learnqa");
    userData.put("lastName", "learnqa");

    Response responseCreateAuth = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
  }
  @Test
  public void testCreateUserSuccessfully(){
    String email = DataGenerator.getRandomEmail();

    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData.put("password", "123");
    userData.put("username", "learnqa");
    userData.put("firstName", "learnqa");
    userData.put("lastName", "learnqa");

    Response responseCreateAuth = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
    Assertions.assertJsonHasField(responseCreateAuth, "id");
  }
  @Test
  public void testCreateUserWithIncorrectEmail() {
    String email = "vinkotovexample.com";

    Map<String, String> userData = new HashMap<>();
    userData.put("email", email);
    userData.put("password", "123");
    userData.put("username", "learnqa");
    userData.put("firstName", "learnqa");
    userData.put("lastName", "learnqa");

    Response responseCreateAuth = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
    Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
  }

  @ParameterizedTest
  @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
  public void testCreateUserWithoutMandatoryField(String field) {
    Map<String, String> userData = DataGenerator.getRegistrationData();
    userData.remove(field);

    Response responseCreateAuthWoField = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseCodeEquals(responseCreateAuthWoField, 400);
    Assertions.assertResponseTextEquals(responseCreateAuthWoField, String.format("The following required params are missed: %s", field));
  }
  @Test
  public void testCreateUserWithShortName() {
    Map<String, String> userData = DataGenerator.getRegistrationData();
    userData.replace( "firstName", "learnqa", "u");

    Response responseCreateUserWithShortName = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseCodeEquals(responseCreateUserWithShortName, 400);
    Assertions.assertResponseTextEquals(responseCreateUserWithShortName, "The value of 'firstName' field is too short");
  }
  @Test
  public void testCreateUserWithLongName() {
    Map<String, String> userData = DataGenerator.getRegistrationData();
    String longFirstName = "firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols1firstNameWithLengthMoreThanTwoHundredFiftySymbols11";
    userData.replace( "firstName", longFirstName);

    Response responseCreateUserWithLongName = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn();

    Assertions.assertResponseCodeEquals(responseCreateUserWithLongName, 400);
    Assertions.assertResponseTextEquals(responseCreateUserWithLongName, "The value of 'firstName' field is too long");
  }
}