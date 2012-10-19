package controllers;

import models.User;

/**
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 *
 */
public class Controller {

	User currentUser = new User();
	DbController database = new DbController();
	
	/**
	 * 
	 * @return
	 */
	public static User getCurrentUser(){
		User currentUser = new User();
		return currentUser;
		
	}
	
	/**
	 * Set the current User
	 */
	public static void setCurrentUser(){
		
	}
	
	/**
	 * Get the instance of the Database Controller
	 * @return
	 */
	public static DbController getDatabase(){
		DbController database = new DbController();
		return database;
		
	}
}
