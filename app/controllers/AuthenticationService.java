package controllers;

import models.*;
import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.*;
import play.mvc.*;
import play.libs.*;

public class AuthenticationService extends Controller {
	private String authToken;
	private static UserAccount currentUser;
	
	/**
	 * Constructor for the service
	 */
	public AuthenticationService()
	{
		authToken = "";
		currentUser = null;
	}
	
	/**
	 * Get the current logged in user
	 */
	public static UserAccount getCurrentUser()
	{
		return currentUser;
	}
	
	public static void setCurrentUser(UserAccount curUser)
	{
		currentUser = curUser;
	}
	
	/**
	 * The login function to verify and login a given user
	 */
	public boolean login(String username, String password)
	{
		UserAccount curUser = UserAccount.find("username = ? And password = ?", username, password).first();
		if (curUser != null && getCurrentUser() == null)
		{
			// user exists and there is currently no user logged in
			// modify the last login time
			curUser.setLastLogin(new Date());
			// set the current logged in user to this user
			setCurrentUser(curUser);
			// save the modifications to this user
			curUser.save();
			// return true
			return true;
		}

		// could not find the user or someone is already logged in so return false
		return false;
	}
	
	/**
	 * The signup function makes sure the username does not exist and then adds the user to the database
	 */
	public boolean signUp(String username, String password, String email)
	{
		if (UserAccount.find("username = ?", username).first() != null || getCurrentUser() != null)
		{
			// user already exists or someone is already logged in so return false
			return false;
		}
		
		UserAccount newUser =  new UserAccount(email, password, username, new Date(), new Date(), false).save();
		setCurrentUser(newUser);
		return true;
	}
	
	/**
	 * Sign out the current logged in user
	 */
	public boolean signOut()
	{
		// set the current user to null and return true
		setCurrentUser(null);
		return true;
	}
	
	public String getAuthToken()
	{
		return authToken;
	}
	
	private void setAuthToken(String token)
	{
		authToken = token;
	}
	
	public boolean openYouTubeAPI()
	{
		// set the paramaters for the request
		String MasterUserName = "";
		String MasterPassword = "";
		String MasterSource = "";
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
			return false;
		}
		// got the token so set the authentication token and return true
		setAuthToken(token);		
		return true;
	}
	

}
