package controllers;

import models.*;
import java.util.List;
import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.*;
import play.mvc.*;
import play.libs.*;

/** 
 * VideoIntegrationService Class
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines the services needed for all Video requirements
 *
 */
public class VideoIntegrationService extends Controller{
	
	/**
	 * getVideos(String owner)
	 * 
	 * Get the videos and metadata from the Youtube, combine this with information stored in the database and return the information
	 * 
	 * @param owner
	 */
	public static void getVideos(String owner)
	{
		// get all of the videos and metadata from Youtube
		
		
		// get all the videos from the database for that username
		List<Video> vidList = Video.find("byOwner", owner).fetch();
		
		// combine the lists
		
		// return the result
	}
	
	/**
	 * deleteVideo(String id)
	 * 
	 * Given the Video Id, delete it and its associated metadata from Youtube as well as from the database
	 * 
	 * @param id
	 */
	public static void deleteVideo(String id)
	{
		// delete it from the database
		Video delVideo = Video.find("byId", id).first();
		if (delVideo != null)
		{
			// found the video in the database
			delVideo.delete();
		}
		else
		{
			// could not find the video in the database
			renderJSON(false);
		}
		
		// delete the video from the Youtube API
		
		// result was successful
		renderJSON(true);
	}
	
	/**
	 * addVideo(String title, String description, List<Tag> tags, String category)
	 * 
	 * Upload the video and metadata to the Youtube as well as upload metadata to the database
	 * 
	 * @param title
	 * @param description
	 * @param tags
	 * @param category
	 */
	public static void addVideo(String title, String description, List<Tag> tags, String category)
	{
		renderJSON(false);
	}
	

	/**
	 * updateVideo(String title, String description, List<Tag> tags, String category)
	 * 
	 * Update the video metadata within Youtube
	 * 
	 * @param title
	 * @param description
	 * @param tags
	 * @param category
	 */
	public static void updateVideo(String title, String description, List<Tag> tags, String category)
	{
		renderJSON(false);
	}
	
	/**
	 * searchVideos(List<Tag> tags)
	 * 
	 * take the list of tags and fetch all relating videos
	 * 
	 * @param tags
	 */
	public static void searchVideos(List<Tag> tags)
	{
		renderJSON(false);
	}
	
	/**
	 * searchVideos(List<Tag> tags, String owner)
	 * 
	 * take the list of tags and the owner and fetch all relating videos
	 * 
	 * @param tags
	 * @param owner
	 */
	public static void searchVideos(List<Tag> tags, String owner)
	{
		renderJSON(false);
	}
	
	/**
	 * getVideoInfo(String id)
	 * 
	 * fetch the metadata for a given video from Youtube, combine it with the information from the database and return the results
	 * 
	 * @param id
	 */
	public static void getVideoInfo(String id)
	{
		renderJSON(false);
	}
	
	/**
	 * likeVideo(String id)
	 * 
	 * Get the video from the database, increment the number of likes by 1, and save the update
	 * 
	 * @param id
	 */
	public static void likeVideo(String id)
	{
		int numLikes = -1;
		// get the video from the database
		Video vid = Video.find("byId", id).first();
		
		if (vid != null)
		{
			// add one to the number of likes
			vid.setLikes(vid.getLikes() + 1);
	
			// save the update to the database
			vid.save();
			
			// set the number of likes to be returned
			numLikes = vid.getLikes();
		}
		
		// return number of likes
		renderJSON(numLikes);		
	}
	
	/**
	 * viewVideo(String id)
	 * 
	 * Get the video from the database, increment the number of views by 1, and save the update
	 * 
	 * @param id
	 */
	public static void viewVideo(String id)
	{
		int numViews = -1;
		// get the video from the database
		Video vid = Video.find("byId", id).first();
		
		if (vid != null)
		{
			// add one to the number of likes
			vid.setViews(vid.getViews() + 1);
		
			// save the update to the database
			vid.save();
			
			// set the number of views to be returned
			numViews = vid.getViews();	
		}
		
		// return number of views
		renderJSON(numViews);
	}
	

}
