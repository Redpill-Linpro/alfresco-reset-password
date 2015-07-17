package com.rplp.alfresco.webscripts.slingshot;

import static com.jayway.restassured.RestAssured.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

public class ResetUserPasswordTest {
  public static final String BASE_URI = "http://localhost:8080/alfresco/service";

  @Before
  public void setUp() {
    RestAssured.authentication = preemptive().basic("admin", "admin");
  }

  @After
  public void tearDown() {
    RestAssured.reset();
  }

  @Test
  public void testResetPasswordInvalidRequest() {
    RestAssured.requestContentType(ContentType.JSON);
    RestAssured.responseContentType(ContentType.JSON);
    
    final String emptyJson = "{ }";
    //given().baseUri(BASE_URI).body(emptyJson).expect().statusCode(400).when().post("/extras/modules/reset-user-password");
  }
}
