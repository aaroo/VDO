package controllers;

import models.*;

import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.*;
import play.mvc.*;
import play.libs.*;

/** 
 * UserIntegrationService Class
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines the services needed for all user requirements
 *
 */
public class UserIntegrationService extends Controller {
		
	/**
	 * searchUser(String username)
	 * 
	 * This function retrieves all user objects from the database that contain the search string
	 * 
	 * @param username
	 */
	public static void searchUser(String username)
	{
		// get the list of users that match a given username
		try
		{
			List<UserAccount> userList = UserAccount.find("byUsernameLike", username).fetch();
			renderJSON(userList);
		}
		catch (Exception e)
		{
			renderJSON(new ArrayList<UserAccount>());
		}
	}
	
	/**
	 * getUserInfo(String username)
	 * 
	 * This obtains the user information from the database for a given username
	 * 
	 * @param username
	 */
	public static void getUserInfo(String username)
	{
		// get the user information for a given username
		try
		{
			UserAccount user = UserAccount.find("byUsername", username).first();
			renderJSON(user);
		}
		catch (Exception e)
		{
			renderJSON(new UserAccount("", "", "", new Date(), new Date(), false));
		}
	}
	
	/**
	 * deleteUser(String username)
	 * 
	 * This function searches for a given user and if found will delete it from the database
	 * 
	 * @param username
	 */
	public static void deleteUser(String username)
	{
		try
		{
			// find the user in the database
			UserAccount curUser = UserAccount.find("byUsername", username).first();
			
			// user exists so delete it
			// check to make sure the current user is not this user
			if (AuthenticationService.getCurrentUser() != null && AuthenticationService.getCurrentUser().getUserName().equals(curUser.getUserName()))
			{
				// set the current user to null
				AuthenticationService.setCurrentUser(null);
			}
			// delete the user
			curUser.delete();
			renderJSON("{\"result\":" + true +"}");
		}
		catch (Exception e)
		{
			// could not find the user or someone has already deleted it so return false
			renderJSON("{\"result\":" + false +"}");
		}

		// could not find the user or someone has already deleted it so return false
		renderJSON("{\"result\":" + false +"}");
	}
	
	/**
	 * editUser(UserAccount modUser)
	 * 
	 * Takes the user that is passed in, searches for the username in the database and will modify the user attributes
	 * 
	 * @param modUser
	 */
	public static void editUser(String username, String password, String email, long bAdmin)
	{
		try
		{
			// get the user from the database
			UserAccount curUser = UserAccount.find("byUsername", username).first();
		
			// make the modifications to the user, first make sure the email is unique
			String useremail = null;
			try
			{
				useremail = UserAccount.find("byEmail", email).first();
			}
			catch (Exception e)
			{
				// do nothing it doesn't exist
			}
			if (!email.equals(curUser.getEmail()) && useremail != null)
			{
				// the email is not unique cannot modify the user
				renderJSON("{\"result\":" + false +"}");
			}
			curUser.setEmail(email);
			curUser.setPassword(password);
			curUser.setAdmin(bAdmin != 0);
			// check to make sure the current user is not this user
			if (AuthenticationService.getCurrentUser() != null && AuthenticationService.getCurrentUser().getUserName().equals(curUser.getUserName()))
			{
				// update the current user to this modified user
				AuthenticationService.setCurrentUser(curUser);
			}
			// try to save the user
			try
			{
				curUser.save();
			}
			catch (Exception e)
			{
				renderJSON("{\"result\":" + false +"}");
			}
			renderJSON("{\"result\":" + true +"}");
		}
		catch (Exception e)
		{
			// could not find the user or someone has already deleted it so return false
			renderJSON("{\"result\":" + false +"}");
		}
		// could not find the user or someone has already deleted it so return false
		renderJSON("{\"result\":" + false +"}");
	}
	
	/**
	 * getAllUsers()
	 * 
	 * Obtains the list of all users from the database
	 * 
	 */
	public static void getAllUsers()
	{
		try
		{		
			// get the list of all users from the database		
			List<UserAccount> userList = UserAccount.findAll();		
			renderJSON(userList);
		}
		catch (Exception e)
		{
			renderJSON(new ArrayList<UserAccount>());
		}
	}
	

}
