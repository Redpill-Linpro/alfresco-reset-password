package com.rplp.alfresco.repo.webscripts.security;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.preemptive;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class ChangeLdapPasswordWebscriptTest {
  public static final String BASE_URI = "http://localhost:8080/alfresco/service";

  private static final String ADMIN_USER_NAME = "admin";

  @Before
  public void setUp() throws Exception {
    RestAssured.authentication = preemptive().basic(ADMIN_USER_NAME, ADMIN_USER_NAME);
  }

  @After
  public void tearDown() throws Exception {
    RestAssured.reset();
  }

  @Test
  public void testExecuteImplWebScriptRequestStatusCache() {
    RestAssured.requestContentType(ContentType.JSON);
    RestAssured.responseContentType(ContentType.JSON);
    
    final String emptyJson = "{ }";
    //Response res = given().baseUri(BASE_URI).body(emptyJson.toString()).expect().statusCode(400).when().post("/rplp/api/changeLdapPassword/" + ADMIN_USER_NAME);
  }
}
