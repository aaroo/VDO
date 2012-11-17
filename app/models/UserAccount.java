package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import org.joda.time.DateTime;

/** 
 * UserAccount Class
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines all requirements needed to implement the User model
 *
 */
@Entity
public class UserAccount extends Model {
	
	@Email
	@Required
	@Column(unique=true)
	public String email;
	
	@Required
	public String password;
	
	@Required
	@Column(unique=true)
	private String username;
	
	@Required
	public boolean isAdmin;
	
	@Required
	private Date joinDate;
	
	@Required 
	private Date lastLogin;
	
	/**
	 * UserAccount(String email, String password, String username, Date joinDate, Date lastLogin, boolean isAdmin)
	 * 
	 * Constructor for the User Account Object
	 * 
	 * @param email
	 * @param password
	 * @param username
	 * @param joinDate
	 * @param lastLogin
	 * @param isAdmin
	 */
	public UserAccount(String email, String password, String username,
		Date joinDate, Date lastLogin, boolean isAdmin) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.isAdmin = isAdmin;
		this.joinDate = joinDate;
		this.lastLogin = lastLogin;
	}
	
	/**
	 * connect(String email, String password)
	 * 
	 * Find the user that has the given Email address and password
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static UserAccount connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}
	
	/**
	 * toString()
	 * 
	 * Return the User object as a string
	 * 
	 * @return
	 */
	public String toString() {
		return email;
	}
	
	/**
	 * getUserName()
	 * 
	 * Get the User name associated with the User
	 * 
	 * @return
	 */
	public String getUserName()
	{
		return username;
	}
	
	/**
	 * setUserName(String name)
	 * 
	 * Set the username for the user
	 * 
	 * @param name
	 */
	public void setUserName(String name)
	{
		username = name;
	}
	
	/**
	 * getEmail()
	 * 
	 * Get the Email for the given user
	 * 
	 * @return
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * setEmail(String emailaddr)
	 * 
	 * Make sure the address is valid by seeing that it contains an @ sign with a period after that and if so set the email address
	 * 
	 * @param emailaddr
	 */
	public void setEmail(String emailaddr)
	{
		// make sure it is a valid address
		if (emailaddr.contains("@") && emailaddr.lastIndexOf(".") > emailaddr.lastIndexOf("@"))
		{
			email = emailaddr;
		}
	}
	
	/**
	 * getPassword()
	 * 
	 * Return the password for the user
	 * 
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}
	
	/**
	 * setPassword(String pass)
	 * 
	 * Set the User's password
	 * 
	 * @param pass
	 */
	public void setPassword(String pass)
	{
		password = pass;
	}
	
	/**
	 * getJoinDate
	 * 
	 * Return the User's join data
	 * 
	 * @return
	 */
	public Date getJoinDate()
	{
		return joinDate;
	}
	
	/**
	 * setJoinDate(Date jDate)
	 * 
	 * Set the User's Join Date
	 * 
	 * @param jDate
	 */
	public void setJoinDate(Date jDate)
	{
		joinDate = jDate;
	}
	
	/**
	 * getLastLogin()
	 * 
	 * Get the User's last login
	 * 
	 * @return
	 */
	public Date getLastLogin()
	{
		return lastLogin;
	}
	
	/**
	 * setLastLogin(Date llog)
	 * 
	 * Set the User's last login in date
	 * 
	 * @param llog
	 */
	public void setLastLogin(Date llog)
	{
		lastLogin = llog;
	}
	
	/**
	 * setAdmin(boolean admin)
	 * 
	 * Set whether or not the User has admin privilege
	 * 
	 * @param admin
	 */
	public void setAdmin(boolean admin)
	{
		isAdmin = admin;
	}
	
	/**
	 * isAdmin()
	 * 
	 * Return the flag indicating whether the User has administrative privileges
	 * 
	 * @return
	 */
	public boolean isAdmin()
	{
		return isAdmin;
	}

}