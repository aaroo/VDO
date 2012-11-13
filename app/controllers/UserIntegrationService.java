package controllers;

import models.*;
import java.util.List;
import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.*;
import play.mvc.*;
import play.libs.*;

public class UserIntegrationService extends Controller {
		
	/**
	 * Constructor for the service
	 */
	public UserIntegrationService()
	{
	}
	
	/**
	 * Search the User list for those that contain the username provided
	 */
	public List<UserAccount> searchUser(String username)
	{
		return UserAccount.find("byUsernameLike", username).fetch();
	}
	
	/**
	 * Obtain and return the user information for a given username
	 */
	public UserAccount getUserInfo(String username)
	{
		return UserAccount.find("byUsername", username).first();
	}
	
	/**
	 * Delete a given user from the database
	 */
	public boolean deleteUser(String username)
	{
		UserAccount curUser = UserAccount.find("byUsername", username).first();
		if (curUser != null)
		{
			// user exists so delete it
			// check to make sure the current user is not this user
			if (AuthenticationService.getCurrentUser() != null && AuthenticationService.getCurrentUser().getUserName().equals(curUser.getUserName()))
			{
				// set the current user to null
				AuthenticationService.setCurrentUser(null);
			}
			// delete the user
			curUser.delete();
			return true;
		}

		// could not find the user or someone has already deleted it so return false
		return false;
	}
	
	/**
	 * Edit a given user in the database
	 */
	public boolean editUser(UserAccount modUser)
	{
		UserAccount curUser = UserAccount.find("byUsername", modUser.getUserName()).first();
		if (curUser != null)
		{
			curUser.setEmail(modUser.getEmail());
			curUser.setPassword(modUser.getPassword());
			curUser.setAdmin(modUser.isAdmin());
			// check to make sure the current user is not this user
			if (AuthenticationService.getCurrentUser() != null && AuthenticationService.getCurrentUser().getUserName().equals(curUser.getUserName()))
			{
				// set the current user to this modified user
				AuthenticationService.setCurrentUser(curUser);
			}
			// delete the user
			curUser.save();
			return true;
		}

		// could not find the user or someone has already deleted it so return false
		return false;
	}
	
	/**
	 * Get the list of all users
	 */
	public List<UserAccount> getAllUsers(UserAccount modUser)
	{
		return UserAccount.findAll();
	}
	

}
