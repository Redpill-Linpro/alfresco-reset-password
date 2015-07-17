package com.rplp.alfresco.repo.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class RandomPasswordGeneratorTest {
  
  @Test
  public void testgeneratePswd() {
    String newPassword = RandomPasswordGenerator.generatePswd(8, 12, 2, 2, 1);
    
    assertTrue(newPassword.length() >= 8);
    assertTrue(newPassword.length() <= 12);
    
    int noOfCAPSAlpha = 0;
    int noOfDigits = 0;
    int noOfSplChars = 0;
    for (int i = 0; i < newPassword.length(); i++) {
      char character = newPassword.charAt(i);
      if (character >= 'A' && character <= 'Z') {
        noOfCAPSAlpha++;
      } else if (character >= '0' && character <= '9') {
        noOfDigits++;
      } else if (containsChar(RandomPasswordGenerator.SPL_CHARS, character)) {
        noOfSplChars++;
      }
    }
    
    assertEquals(2, noOfCAPSAlpha);
    assertEquals(2, noOfDigits);
    assertEquals(1, noOfSplChars);
  }
  
  private boolean containsChar(String s, char c) {
    if (s.length() == 0) {
      return false;
    } else {
      return s.charAt(0) == c || containsChar(s.substring(1), c);
    }
  }

}
