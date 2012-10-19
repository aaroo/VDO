package models;

import java.lang.reflect.Array;
import java.util.List;
import models.Video;

import org.joda.time.DateTime;


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
	private List<Video> videoList;
	private boolean bAdmin;
	
	
	/**
	 * Default Constructor for the User Class
	 */
	public User()
	{
		username = "";
		email = "";
		password = "";
		joinDate = new DateTime(0, 0, 0, 0, 0);
		lastLogin = new DateTime(0,0,0,0,0);
		videoList = new List<Video>();
		bAdmin = false;
	}
	
	/**
	 * Constructor to specify the different user attributes 
	 */
	public User(String name, String emailaddr, String pass, DateTime jDate, DateTime llogin, List<Video> vList, boolean admin)
	{
		username = name;
		email = emailaddr;
		password = pass;
		joinDate = jDate;
		lastLogin = llogin;
		videoList = vList;
		bAdmin = admin;
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
	public DateTime getJoinDate()
	{
		return joinDate;
	}
	
	/**
	 * Set the join date for the user
	 */
	public void setJoinDate(DateTime jDate)
	{
		joinDate = jDate;
	}
	
	/**
	 * Get the last login for the user
	 */
	public DateTime getLastLogin()
	{
		return lastLogin;
	}
	
	/**
	 * Set the last login for the user
	 */
	public void setLastLogin(DateTime llog)
	{
		lastLogin = llog;
	}
	
	/**
	 * Get the Video List for the user
	 */
	public List<Video> getVideoList()
	{
		return videoList;
	}
	
	/**
	 * Set the Video List for the user
	 */
	public void setVideoList(List<Video> vList)
	{
		videoList = vList;
	}
	
	/**
	 * Replace a video in the user list with a new video
	 */
	public boolean setVideo(Video Old, Video New)
	{
		return true;
	}
	
	/**
	 * Determine if the User has Curator or Admin rights
	 */
	public boolean isPrivileged()
	{
		// Determine this based on User name
		// If the username is set then the person has privileged rights
		if (username != "")
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Determine if the user is an Admin
	 */
	public booleain isAdmin()
	{
		return bAdmin;
	}

}
