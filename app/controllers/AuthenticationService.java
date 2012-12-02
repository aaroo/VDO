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
	 * getCurrentUserInfo()
	 * 
	 * Return the currently signed in user as a json feed
	 * 
	 * @return
	 */
	public static void getCurrentUserInfo()
	{
		// return the current user
		if (currentUser != null)
		{
			renderJSON(currentUser);
		}
		else
		{
			// user does not exist
			renderJSON("{}");
		}
	}
	
	/**
	 * setCurrentUser(UserAccount curUser)
	 * 
	 * Set the Current user of the program, use the @Util identify to avoid redirection
	 * 
	 * @param curUser
	 */
	@Util
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
		try
		{		
			UserAccount curUser = UserAccount.find("byEmailAndPassword", email, password).first();
			
			if (AuthenticationService.getCurrentUser() != null)
			{
				// somebody is already logged in
				renderJSON("{\"result\":" + false + ",\"status\":\"alreadyLoggedIn\"}");
			}
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
				renderJSON("{\"result\":" + false +"}");
			}
			// return true
			renderJSON("{\"result\":" + true +"}");
		}
		catch(Exception e)
		{
			// could not find the user or someone is already logged in so return false
			renderJSON("{\"result\":" + false +"}");
		}

		// could not find the user or someone is already logged in so return false
		renderJSON("{\"result\":" + false +"}");
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
		try
		{
			if (AuthenticationService.getCurrentUser() != null || UserAccount.find("byEmail", email).first() != null || UserAccount.find("byUsername", username).first() != null)
			{
				// user already exists or someone is already logged in so return false
				renderJSON("{\"result\":" + false + ",\"status\":\"alreadyLoggedIn\"}");
			}
		}
		catch (Exception e)
		{
			/* ignore the exception entry does not exist */
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
			renderJSON("{\"result\":" + false +"}");
		}
				
		// return success
		renderJSON("{\"result\":" + true +"}");
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
		if (AuthenticationService.getCurrentUser() != null)
		{
			AuthenticationService.setCurrentUser(null);
			renderJSON("{\"result\":" + true +"}");
		}
		else
		{
			renderJSON("{\"result\":" + false +"}");
		}
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
	 * Set the YouTube Service for interacting with the YouTube API, specify the @Util function to avoid redirection
	 * 
	 * @param token
	 */
	@Util
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
		if (AuthenticationService.getYouTubeService() == null)
		{
			String clientID = "Drexel VDO";
			String developer_key = "AI39si486NEocHWHv8P-hJgwYvUAaXSbImRn4EtClWtCc2kaGiGsPGt-Rj3qZ8cOJPV-uxcptia2EamACPgk3zHsZNOnvpkypQ";
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
				renderJSON("{\"result\":" + false +"}");
			}
			// successfully connected to YouTube API
			renderJSON("{\"result\":" + true +"}");
		}
		else
		{
			// already open
			renderJSON("{\"result\":" + true +"}");
		}
	}
	

}
