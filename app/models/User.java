package models;

import java.lang.reflect.Array;

import org.joda.time.DateTime;


//import interfaces.YTPlayer;
/** 
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines the User Model to store all user related information
 *
 */
public class User {
	private String username;
	private String email;
	private String password;
	private DateTime joinDate;
	private DateTime lastLogin;
//	private Array videoList;
	private boolean bAdmin;
	
	
	/**
	 * Default Constructor for the User Class
	 */
	public User()
	{
		username = "";
		email = "";
		password = "";
	//	joinDate = 0;
	//	lastLogin = 0;
	//	videoList.
	}
	/**
	 * Get the username for the user
	 */
	public String getUserName()
	{
		return username;
	}
	
	/**
	 * Set the username for the user
	 */
	public void setUserName(String name)
	{
		username = name;
	}
	
	/**
	 * Get the email address for the user
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Set the email address for the user
	 */
	public void setEmail(String emailaddr)
	{
		email = emailaddr;
	}
	
	/**
	 * Get the password for the user
	 */
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * Set the password for the user
	 */
	public void setPassword(String pass)
	{
		password = pass;
	}
	
	/**
	 * Get the join date for the user
	 */
//	public DateTime getJoinDate()
//	{
//		return joinDate;
//	}
	
	/**
	 * Set the join date for the user
	 */
//	public void setJoinDate(DateTime jDate)
//	{
//		joinDate = jDate;
//	}
	
	/**
	 * Get the last login for the user
	 */
//	public DateTime getLastLogin()
//	{
//		return lastLogin;
//	}
	
	/**
	 * Set the last login for the user
	 */
//	public void setLastLogin(DateTime llog)
//	{
//		lastLogin = llog;
//	}
	
	/**
	 * Get the Video List for the user
	 */
	

}
