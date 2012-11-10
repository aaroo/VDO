package controllers;

import play.*;
import play.mvc.*;
import org.joda.time.DateTime;
import java.util.*;
import play.data.validation.*;

import models.*;

public class Application extends Controller {

    public static void index() {
        render();
    }

	public static void makeNewVid(
		int userID,
		@Required(message="Title must be specified.") String vTitle,
		String desc)
	{		
		UserAccount user = null;
		if (userID == 0)
		{
			if (user == null)
			{
				user = new UserAccount("jax2@fake.com", "jaxBiker51", "jaxTeller", new Date(), new Date(), false);
				user.save();
			}
		}
		else
		{
			user = UserAccount.findById(userID);
		}
	}
	
	public static void success(String username){
		render(username);
	}
	
	public static void makeNewUser(
		@Required @Email(message="Email is required") String email,
		 @Required String password,
		 @Required String userName){				
				
		UserAccount newUser = new UserAccount(email, password, userName, new Date(), new Date(), false);
		newUser.save();
		
		success(userName);
		//flash.success("User %s created", userName);
	}
	
	
}