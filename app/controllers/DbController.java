package controllers;

import models.Video;
import models.User;
import java.util.List;
/**
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 *
 */
public class DbController {
	
	/**
	 * Check to see if the username and password is valid
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isUser(String username, String password)
	{
		return false;
	}
	
	/**
	 * Add the user to the database
	 */
	public boolean addUser(User newUser)
	{
		return false;
	}
	
	/**
	 * Modify the user in the database
	 */
	public boolean modifyUser(User modUser)
	{
		return false;
	}
	
	/**
	 * Delete the User from the database
	 */
	public boolean deleteUser(User delUser)
	{
		return false;
	}
	
	/**
	 * Get the User Role from the database
	 */
	public String getUserRole(User user)
	{
		return "";
	}
	
	/**
	 * Get the List of Users from the database
	 */
	public List<User> getUserList()
	{
		return new List<User>();
	}
	
	/**
	 * Add the following video to the following User
	 */
	public boolean addVideo(User owner, Video vid)
	{
		return false;
	}
	
	/**
	 * Modify the video in the database
	 */
	public boolean modifyVideo(Video modVid)
	{
		return false;
	}
	
	/**
	 * Delete the video from the database
	 */
	public boolean deleteVideo(Video delVid)
	{
		return false;
	}
	
	/**
	 * Get all the videos from the database
	 */
	public List<Video> getVideoList()
	{
		return new List<Video>();
	}
	
	/**
	 * Get the video list for a user
	 */
	public List<Video> getVideoList(User owner)
	{
		return new List<Video>();
	}
	
	/**
	 * Search for a Video based on the keywords
	 */
	public List<Video> searchVideo(List<String> keywords)
	{
		return new List<Video>();
	}
	
	/**
	 * Search for a given user based on a username keyword
	 */
	public List<User> searchUser(String username)
	{
		return new List<User>();
	}

}
