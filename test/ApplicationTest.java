import javax.xml.ws.Response;

import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import org.junit.*;
import play.libs.*;
import org.junit.*;
import java.util.*;
import org.json.JSONObject;

import models.*;
 
public class ApplicationTest extends FunctionalTest {
     
	/* functional test to check the sign in capability */
	@Test
    public void testSignUp() {
		Http.Response response = GET("/authenticationservice/signout");
		/* try a valid sign up request request */
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco1");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco1@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    String json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        /* try doing it again, it should fail since the user is already signed in */
        signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco1");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco1@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":false}"));
    }
	
	/* functional test to check the sign out capability */
	@Test
    public void testSignOut() {
		Http.Response response = GET("/authenticationservice/signout");
		/* make sure a user is signed in */
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco2");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco2@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
		/* this should be successful */
	    response = GET("/authenticationservice/signout");
	    String json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        /* this should fail since the user is already signed out */
        response = GET("/authenticationservice/signout");
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":false}"));
    }
	
	/* functional test to check the login capability */
	@Test
    public void testLogin() {
		Http.Response response = GET("/authenticationservice/signout");
		/* this should be false as the user does not exist */
		Map<String, String> loginParams = new HashMap<String, String>();
		loginParams.put("email", "dco3@drexel.edu");
		loginParams.put("password", "password");
		response = POST("/authenticationservice/login", loginParams);
	    String json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":false}"));
        /* try a valid sign up request request */
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco3");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco3@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    response = GET("/authenticationservice/signout");
		/* this should be true */
		loginParams = new HashMap<String, String>();
		loginParams.put("email", "dco3@drexel.edu");
		loginParams.put("password", "password");
	    response = POST("/authenticationservice/login", loginParams);
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        /* this should be false as a user is already logged in */
		loginParams = new HashMap<String, String>();
		loginParams.put("email", "dco3@drexel.edu");
		loginParams.put("password", "password");
	    response = POST("/authenticationservice/login", loginParams);
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":false}"));
    }
	
	/* functional test to check the connectivity to the YouTube API */
    @Test
    public void testYouTubeConnection() {
    	Http.Response response = GET("/authenticationservice/openyoutubeapi");
    	String json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        assertFalse(json.equals("{\"result\":false}"));
    }
    
    /* functional test to search for a user */
    @Test
    public void testSearchUser() {
    	Http.Response response = GET("/authenticationservice/signout");
        /* try a valid sign up request request */
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco4");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco4@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    response = GET("/authenticationservice/signout");
    	/* should be able to find the user */
		Map<String, String> searchUserParams = new HashMap<String, String>();
		searchUserParams.put("username", "dco4");
	    response = POST("/userintegrationservice/searchuser", searchUserParams);
	    String json = getContent(response).toString();
	    assertTrue(json.length() > 0);
        assertTrue(json.contains("\"username\":\"dco4\""));
        /* should not be able to find the user */
        searchUserParams = new HashMap<String, String>();
		searchUserParams.put("username", "dco55");
	    response = POST("/userintegrationservice/searchuser", searchUserParams);
	    json = getContent(response).toString();
        assertFalse(json.contains("username\":\"dco55\""));
    }
    
    /* functional test to get user info */
    @Test
    public void testGetUserInfo() {
    	Http.Response response = GET("/authenticationservice/signout");
        /* try a valid sign up request request */
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco5");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco5@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    response = GET("/authenticationservice/signout");
    	/* should be able to find the user */
		Map<String, String> getUserInfoParams = new HashMap<String, String>();
		getUserInfoParams.put("username", "dco5");
	    response = POST("/userintegrationservice/searchuser", getUserInfoParams);
	    String json = getContent(response).toString();
	    assertTrue(json.length() > 0);
        assertTrue(json.contains("\"username\":\"dco5\""));
        /* should not be able to find the user */
        getUserInfoParams = new HashMap<String, String>();
        getUserInfoParams.put("username", "dco55");
	    response = POST("/userintegrationservice/searchuser", getUserInfoParams);
	    json = getContent(response).toString();
        assertFalse(json.contains("username\":\"dco55\""));
    }
    
    /* functional test to check the deletion of a user */
	@Test
    public void testDeleteUser() {
		/* try a valid request to sign up a user */
		Http.Response response = GET("/authenticationservice/signout");
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco6");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco6@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    String json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        /* try deleting the user */
        Map<String, String> deleteParams = new HashMap<String, String>();
        deleteParams.put("username", "dco6");
	    response = POST("/userintegrationservice/deleteuser", deleteParams);
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        /* try doing it again, it should fail */
        deleteParams = new HashMap<String, String>();
        deleteParams.put("username", "dco6");
	    response = POST("/userintegrationservice/deleteuser", deleteParams);
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":false}"));
    } 
	
	/* functional test to check the editing of a user */
	@Test
    public void testEditUser() {
		/* try a valid request to sign up a user */
		Http.Response response = GET("/authenticationservice/signout");
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco7");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco7@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    String json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        /* try editing the user */
        Map<String, String> editParams = new HashMap<String, String>();
        editParams.put("username", "dco7");
        editParams.put("password", "newpassword");
        editParams.put("email", "dcotemp7@drexel.edu");
        editParams.put("bAdmin", "0");
	    response = POST("/userintegrationservice/edituser", editParams);
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
    } 
	/* functional test to check the editing of a user */
	@Test
    public void testGetAllUser() {
		/* try a valid request to sign up a user */
		Http.Response response = GET("/authenticationservice/signout");
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco9");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco9@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    String json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        response = GET("/authenticationservice/signout");
        signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco10");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco10@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    json = getContent(response).toString();
        assertTrue(json.equals("{\"result\":true}"));
        response = GET("/authenticationservice/signout");
        /* try getting the users */
		response = GET("/userintegrationservice/getallusers");
	    json = getContent(response).toString();
        assertTrue(json.contains("\"username\":\"dco9\""));
        assertTrue(json.contains("\"username\":\"dco10\""));
        assertFalse(json.contains("\"username\":\"dco11\""));
    } 
}