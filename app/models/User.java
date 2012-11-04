package models;

import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;


/** 
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines the User Model to store all user related information
 *
 */
@Entity
public class User extends Model {
	
	@Required
	private String username;
	@Required
	private String email;
	@Required
	private String password;
	@Required
	private DateTime joinDate;
	@Required
	private DateTime lastLogin;
	@Required
	private boolean bAdmin;
	
	
	/**
	 * Default Constructor for the User class
	 */
	public User()
	{
		username = "";
		email = "";
		password = "";
		joinDate = new DateTime(0, 0, 0, 0, 0);
		lastLogin = new DateTime(0,0,0,0,0);
		bAdmin = false;
	}
	
	/**
	 * Contructor for generating a specific user
	 * @param name
	 * @param emailaddr
	 * @param pass
	 * @param jDate
	 * @param llogin
	 * @param admin
	 */
	public User(String name, String emailaddr, String pass, DateTime jDate, DateTime llogin, boolean admin)
	{
		username = name;
		email = emailaddr;
		password = pass;
		joinDate = jDate;
		lastLogin = llogin;
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
	 * Set administrative rights
	 */
	public void setAdmin(boolean admin)
	{
		bAdmin = admin;
	}
	
	/**
	 * Determine if the user is an Admin
	 */
	public boolean isAdmin()
	{
		return bAdmin;
	}

}
