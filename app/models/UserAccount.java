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
	
	public void setLastLogin(Date d) {
		this.lastLogin = d;
	}
	
	public String toString() {
		return email;
	}
}