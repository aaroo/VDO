package controllers;

import models.*;
import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.*;
import play.mvc.*;
import play.libs.*;
import com.google.gdata.client.*;
import com.google.gdata.client.youtube.*;
import com.google.gdata.data.*;
import com.google.gdata.data.geo.impl.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.media.mediarss.*;
import com.google.gdata.data.youtube.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.io.File;
import java.net.URL;

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
	private static YouTubeService oYouTubeService = null;
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
	 * getYouTubeService()
	 * 
	 * Return the YouTube Service object used for interacting with the Youtube API
	 * 
	 * @return
	 */
	public static YouTubeService getYouTubeService()
	{
		// return the authentication string
		return oYouTubeService;
	}
	
	/**
	 * setYouTubeService(YouTubeService youtubeService)
	 * 
	 * Set the YouTube Service for interacting with the YouTube API
	 * 
	 * @param token
	 */
	private static void setYouTubeService(YouTubeService youtubeService)
	{
		// set the authentication string
		oYouTubeService = youtubeService;
	}
	
	/**
	 * openYouTubeAPI()
	 * 
	 * Open the YouTube API for our project and set the service object to be stored later
	 * 
	 */
	public static void openYouTubeAPI()
	{
		// set the paramaters for the request
		String clientID = "";
		String developer_key = "";
		String MasterUserName = "vdo575@gmail.com";
		String MasterPassword = "vvddoo575";
		YouTubeService service = new YouTubeService(clientID, developer_key);
		try
		{
			// set the user credentials
			service.setUserCredentials(MasterUserName, MasterPassword);
			// store the service for later usage
			AuthenticationService.setYouTubeService(service);
		}
		catch (AuthenticationException e)
		{
			// the authentication was invalid
			renderJSON(false);
		}
		// successfully connected to YouTube API
		renderJSON(true);
	}
	

}
