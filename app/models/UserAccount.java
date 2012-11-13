package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;
import org.joda.time.DateTime;

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
	
	public UserAccount(String email, String password, String username,
		Date joinDate, Date lastLogin, boolean isAdmin) {
		this.email = email;
		this.password = password;
		this.username = username;
		this.isAdmin = isAdmin;
		this.joinDate = joinDate;
		this.lastLogin = lastLogin;
	}
	
	public static UserAccount connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}
	
	public String toString() {
		return email;
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
	public Date getJoinDate()
	{
		return joinDate;
	}
	
	/**
	 * Set the join date for the user
	 */
	public void setJoinDate(Date jDate)
	{
		joinDate = jDate;
	}
	
	/**
	 * Get the last login for the user
	 */
	public Date getLastLogin()
	{
		return lastLogin;
	}
	
	/**
	 * Set the last login for the user
	 */
	public void setLastLogin(Date llog)
	{
		lastLogin = llog;
	}
	
	/**
	 * Set administrative rights
	 */
	public void setAdmin(boolean admin)
	{
		isAdmin = admin;
	}
	
	/**
	 * Determine if the user is an Admin
	 */
	public boolean isAdmin()
	{
		return isAdmin;
	}

}