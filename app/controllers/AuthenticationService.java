package controllers;

import models.*;
import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.*;
import play.mvc.*;
import play.libs.*;

/** 
 * AuthenticationService Class
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines the services needed for all authentication requirements
 *
 */
public class AuthenticationService extends Controller {
	// Authentication string for Youtube API
	private static String authToken = "";
	// The Current user for the program
	private static UserAccount currentUser = null;
	
	/**
	 * getCurrentUser()
	 * 
	 * Return the currently signed in user
	 * 
	 * @return
	 */
	public static UserAccount getCurrentUser()
	{
		// return the current user
		return currentUser;
	}
	
	/**
	 * setCurrentUser(UserAccount curUser)
	 * 
	 * Set the Current user of the program
	 * 
	 * @param curUser
	 */
	public static void setCurrentUser(UserAccount curUser)
	{
		// set the current user
		currentUser = curUser;
	}
	
	/**
	 * login(String email, String password)
	 * 
	 * Make sure the user exists in the database and that a user is not signed in, if this is the case log the user in and return
	 * 
	 * @param email
	 * @param password
	 */
	public static void login(String email, String password)
	{
		// get the current user based on the username and password
		UserAccount curUser = UserAccount.find("byEmailAndPassword", email, password).first();
		if (curUser != null && AuthenticationService.getCurrentUser() == null)
		{
			// user exists and there is currently no user logged in
			// modify the last login time
			curUser.setLastLogin(new Date());
			// set the current logged in user to this user
			AuthenticationService.setCurrentUser(curUser);
			// save the modifications to this user
			try
			{
				curUser.save();
			}
			catch (Exception e)
			{
				renderJSON(false);
			}
			// return true
			renderJSON(true);
		}

		// could not find the user or someone is already logged in so return false
		renderJSON(false);
	}
	
	/**
	 * signUp(String username, String password, String email)
	 * 
	 * make sure the username or email does not exist, if not, add it to the databasee and set the current user to this user
	 * 
	 * @param username
	 * @param password
	 * @param email
	 */
	public static void signUp(String username, String password, String email)
	{
		// check to see if the username already exists and that a current user is not already set
		if (UserAccount.find("byEmail", email).first() != null || UserAccount.find("byUsername", username).first() != null || AuthenticationService.getCurrentUser() != null)
		{
			// user already exists or someone is already logged in so return false
			renderJSON(false);
		}
		
		// create the new user and save it in the database
		try 
		{
				UserAccount newUser =  new UserAccount(email, password, username, new Date(), new Date(), false).save();
				// set the current user to this user
				AuthenticationService.setCurrentUser(newUser);
		}
		catch (Exception e)
		{
			renderJSON(false);
		}
				
		// return success
		renderJSON(true);
	}
	
	/**
	 * signOut()
	 * 
	 * Set the current user to null and return
	 * 
	 */
	public static void signOut()
	{
		// set the current user to null and return true
		AuthenticationService.setCurrentUser(null);
		renderJSON(true);
	}
	
	/**
	 * getAuthToken()
	 * 
	 * Return the Authentication token used for the YouTube API
	 * 
	 * @return
	 */
	public static String getAuthToken()
	{
		// return the authentication string
		return authToken;
	}
	
	/**
	 * setAuthToken(String token)
	 * 
	 * Set the Authentication token for the YouTube API
	 * 
	 * @param token
	 */
	private static void setAuthToken(String token)
	{
		// set the authentication string
		authToken = token;
	}
	
	/**
	 * openYouTubeAPI()
	 * 
	 * Open the YouTube API for our project and set the Authentication token that is returned from the post
	 * 
	 */
	public static void openYouTubeAPI()
	{
		// set the paramaters for the request
		String MasterUserName = "vdo575@gmail.com";
		String MasterPassword = "vvddoo575";
		String MasterSource = "Login for master YouTube Account";
		String loginUrl = "https://www.google.com/accounts/ClientLogin";
		Map<String,String> params = new HashMap<String, String>();
		params.put("Email", MasterUserName);
		params.put("Passwd", MasterPassword);
		params.put("service", "youtube");
		params.put("source", MasterSource);
		// post the request and get the response
		WS.HttpResponse resp = WS.url(loginUrl)
                        .setParameters(params)
                        .post();
		// get the authentication token
		String token = resp.getJson().getAsJsonObject().get("Auth").getAsString();
		if (token == "")
		{
			// authentication token was not set, error with opening YouTube API
			renderJSON(false);
		}
		// got the token so set the authentication token and return success
		AuthenticationService.setAuthToken(token);		
		renderJSON(true);
	}
	

}
