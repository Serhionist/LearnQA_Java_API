import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringLen {
  @Test
  public void stringLen(){
    String string = "Hello, World!";
    int actualLength = string.length();
    assertTrue(actualLength >= 10, "String length is less than 15 characters");

  }
}
