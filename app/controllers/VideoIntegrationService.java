package controllers;

import models.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gdata.client.*;
import com.google.gdata.client.Query.*;
import com.google.gdata.client.youtube.*;
import com.google.gdata.data.*;
import com.google.gdata.data.docs.*;
import com.google.gdata.client.docs.*;
import com.google.gdata.data.acl.*;
import com.google.gdata.data.geo.impl.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.media.mediarss.*;
import com.google.gdata.data.youtube.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import org.json.JSONObject;

import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.*;
import play.mvc.*;
import play.libs.*;
import sun.security.krb5.internal.ccache.Tag;

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
		String videoFeedUrl = "https://gdata.youtube.com/feeds/api/videos";
		// setup the initial query
		VideoFeed videoFeed = null;
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON(new JSONObject(videoFeed).toString());
		}
		try
		{
			YouTubeQuery query = new YouTubeQuery(new URL(videoFeedUrl));
			// set to filter just the programs videos
			query.setAuthor("vdo575");
			// set to order by published videos
			query.setOrderBy(YouTubeQuery.OrderBy.PUBLISHED);
			// set it to grab just the user's videos if set otherwise ignore it and grab all videos
			if (owner != "" && owner != " ")
			{
				CategoryFilter categoryFilter = new CategoryFilter();
				categoryFilter.addCategory(new Category(YouTubeNamespace.DEVELOPER_TAG_SCHEME, owner));
				query.addCategoryFilter(categoryFilter);
			}
			
			// get the video feed from YouTube
			videoFeed = AuthenticationService.getYouTubeService().query(query, VideoFeed.class);
			
			// set the number of likes and views for each entry
			for (VideoEntry videoEntry : videoFeed.getEntries())
			{
				// get the media group for the video feed
				YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
				// get the video in the database that is associated with the video id
				Video video = Video.find("byId", mediaGroup.getVideoId()).first(); 
				// get the video statistics for the video
				YtStatistics stats = videoEntry.getStatistics();
				if(stats != null && video != null ) 
				{
					// set the number of views for the video
					stats.setViewCount(video.getViews());
					// set the number of likes for the video
					stats.setFavoriteCount(video.getLikes());	
				}
			}
			
			// return the result
			renderJSON(new JSONObject(videoFeed).toString());
		}
		catch (Exception e)
		{
			// error getting the user's videos
			renderJSON(new JSONObject(videoFeed).toString());
		}
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
		// get the video to delete from YouTube
		String deleteUrl = "https://gdata.youtube.com/feeds/api/users/default/uploads/" + id;
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON("{\"result\": " + false +"}");
		}
		try
		{
			VideoEntry videoEntry = AuthenticationService.getYouTubeService().getEntry(new URL(deleteUrl), VideoEntry.class);
		
			// check to make sure the VideoEntry is valid
			if (videoEntry != null)
			{
				// delete the video
				videoEntry.delete();
			}
			else
			{
				// could not find the video from YouTube
				renderJSON("{\"result\": " + false +"}");
			}
			
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
				renderJSON("{\"result\": " + false +"}");
			}
			
			// result was successful
			renderJSON("{\"result\": " + true +"}");
		}
		catch (Exception e)
		{
			// error interacting with YouTube API
			renderJSON("{\"result\": " + false +"}");
		}
	}
	
	/**
	 * addVideo(String filename, String title, String description, List<VideoTag> tags, String category)
	 * 
	 * Upload the video and metadata to the Youtube as well as upload metadata to the database
	 * 
	 * @param filename
	 * @param title
	 * @param description
	 * @param tags
	 * @param category
	 */
	public static void addVideo(String filename, String title, String description, List<VideoTag> tags, String category)
	{
		String videoId = "";
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON("{\"id\": " + videoId +"}");
		}
		// create a new video entry for the upload
		VideoEntry newEntry = new VideoEntry();
		
		// get the media group for the new entry
		YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
		mg.setTitle(new MediaTitle());
		// set the title of the video
		mg.getTitle().setPlainTextContent(title);
		// set the category of the video
		mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, category));
		mg.setKeywords(new MediaKeywords());
		for (int iI = 0; iI < tags.size(); iI++)
		{
			// set the tags/keywords for the video
			mg.getKeywords().addKeyword(tags.get(iI).toString());
		}
		mg.setDescription(new MediaDescription());
		// set the description for the video
		mg.getDescription().setPlainTextContent(description);
		mg.setPrivate(false);
		// add a developer tag for this user
		mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, AuthenticationService.getCurrentUser().getUserName()));
		// add the user category
		mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, category));

		// set the video location
		newEntry.setLocation("Philadelphia, PA");
		
		// set the media file source
		String mimeType = DocumentListEntry.MediaType.fromFileName(filename).getMimeType();
		MediaFileSource ms = new MediaFileSource(new File(filename), mimeType);
		newEntry.setMediaSource(ms);
		
		// try uploading the video to YouTube
		try
		{
			// try uploading the video metadata to Youtube and get the token
			URL uploadUrl = new URL("http://uploads.gdata.youtube.com/feeds/api/users/default/uploads");
			VideoEntry createdEntry = AuthenticationService.getYouTubeService().insert(uploadUrl, newEntry);
			if (createdEntry.getId() != "")
			{
				videoId = createdEntry.getId();
				// add the video to the database
				Video video = new Video(videoId, new Date(), AuthenticationService.getCurrentUser().getUserName(), title, description, category, tags, 0, 0).save();
				// return the video id
				renderJSON("{\"id\": " + videoId +"}");
			}

		}
		catch (Exception e)
		{
			// error uploading the video
			renderJSON("{\"id\": " + videoId +"}");
		}
		
		// failed to upload video
		renderJSON("{\"id\": " + videoId +"}");
	}
	

	/**
	 * updateVideo(String title, String description, List<VideoTag> tags, String category)
	 * 
	 * Update the video metadata within Youtube
	 * 
	 * @param title
	 * @param description
	 * @param tags
	 * @param category
	 */
	public static void updateVideo(String id, String title, String description, List<VideoTag> tags, String category)
	{
		// get the video feed to update the metadata in YouTube
		String videoUrl = "https://gdata.youtube.com/feeds/api/users/default/uploads/" + id;
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON("{\"result\": " + false +"}");
		}
		try
		{
			VideoEntry videoEntry = AuthenticationService.getYouTubeService().getEntry(new URL(videoUrl), VideoEntry.class);
				
			// check to make sure the VideoEntry is valid
			if (videoEntry != null)
			{
				// update the video's metadata
				// update the title of the video
				videoEntry.getMediaGroup().getTitle().setPlainTextContent(title);
				// update the description
				videoEntry.getMediaGroup().getDescription().setPlainTextContent(description);
				// update the video tags
				videoEntry.getMediaGroup().getKeywords().clearKeywords();
				for (int iI = 0; iI < tags.size(); iI++)
				{
					videoEntry.getMediaGroup().getKeywords().addKeyword(tags.get(iI).toString());
				}
				// update the category
				videoEntry.getMediaGroup().clearCategories();
				videoEntry.getMediaGroup().addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, category));
				// save the updates
				videoEntry.update();
			}
			else
			{
				// could not find the video from YouTube
				renderJSON("{\"result\": " + false +"}");
			}
			
			// Update the database with this data just to be accurate
			Video video = Video.find("byId", id).first();
			if (video != null)
			{
				// set the video title
				video.setTitle(title);
				// set the video description
				video.setDesc(description);
				// set the video tags
				video.setTags(tags);
				// set the category
				video.setCategory(category);
				// save the updates
				video.save();
			}
			else
			{
				renderJSON("{\"result\": " + false +"}");
			}
			// result was successful
			renderJSON("{\"result\": " + true +"}");
		}
		catch (Exception e)
		{
			// error interacting with YouTube API
			renderJSON("{\"result\": " + false +"}");
		}
	}
	
	/**
	 * searchVideos(List<VideoTag> tags)
	 * 
	 * take the list of tags and fetch all relating videos
	 * 
	 * @param tags
	 */
	public static void searchVideos(List<VideoTag> tags)
	{
		// get all of the videos and metadata from Youtube
		String videoFeedUrl = "https://gdata.youtube.com/feeds/api/videos";
		// initialize the return structure
		VideoFeed videoFeed = null;
		// setup the initial query
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON(new JSONObject(videoFeed).toString());
		}
		try
		{
			YouTubeQuery query = new YouTubeQuery(new URL(videoFeedUrl));
			// set to filter just the programs videos
			query.setAuthor("vdo575");
			// set to order by published videos
			query.setOrderBy(YouTubeQuery.OrderBy.PUBLISHED);
			// set it to search based on the tags
			for (int iI = 0; iI < tags.size(); iI++)
			{
				CategoryFilter catFilter = new CategoryFilter();
				catFilter.addCategory(new Category(YouTubeNamespace.KEYWORD_SCHEME, tags.get(iI).toString()));
				query.addCategoryFilter(catFilter);
			}
			
			// get the video feed from YouTube
			videoFeed = AuthenticationService.getYouTubeService().query(query, VideoFeed.class);
				
			// set the number of likes and views for each entry
			for (VideoEntry videoEntry : videoFeed.getEntries())
			{
				// get the media group for the video feed
				YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
				// get the video in the database that is associated with the video id
				Video video = Video.find("byId", mediaGroup.getVideoId()).first();
				// get the video statistics for the video
				YtStatistics stats = videoEntry.getStatistics();
				if(stats != null && video != null ) 
				{
					// set the number of views for the video
					stats.setViewCount(video.getViews());
					// set the number of likes for the video
					stats.setFavoriteCount(video.getLikes());
				}
			}
			
			// return the result
			renderJSON(new JSONObject(videoFeed).toString());
		}
		catch (Exception e)
		{
			// error get the user's videos
			renderJSON(new JSONObject(videoFeed).toString());
		}
	}
	
	/**
	 * searchVideos(List<VideoTag> tags, String owner)
	 * 
	 * take the list of tags and the owner and fetch all relating videos
	 * 
	 * @param tags
	 * @param owner
	 */
	public static void searchVideos(List<VideoTag> tags, String owner)
	{
		// get all of the videos and metadata from Youtube
		String videoFeedUrl = "https://gdata.youtube.com/feeds/api/videos";
		// Initialize the return structure
		VideoFeed videoFeed = null;
		// setup the initial query
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON(new JSONObject(videoFeed).toString());
		}
		try
		{
			YouTubeQuery query = new YouTubeQuery(new URL(videoFeedUrl));
			// set to filter just the programs videos
			query.setAuthor("vdo575");
			// set to order by published videos
			query.setOrderBy(YouTubeQuery.OrderBy.PUBLISHED);
			// add the developer tag to grab just the owner videos
			CategoryFilter categoryFilter = new CategoryFilter();
			categoryFilter.addCategory(new Category(YouTubeNamespace.DEVELOPER_TAG_SCHEME, owner));
			query.addCategoryFilter(categoryFilter);
			// set it to search based on the tags
			for (int iI = 0; iI < tags.size(); iI++)
			{
				CategoryFilter catFilter = new CategoryFilter();
				catFilter.addCategory(new Category(YouTubeNamespace.KEYWORD_SCHEME, tags.get(iI).toString()));
				query.addCategoryFilter(catFilter);
			}
			
			// get the video feed from YouTube
			videoFeed = AuthenticationService.getYouTubeService().query(query, VideoFeed.class);
				
			// set the number of likes and views for each entry
			for (VideoEntry videoEntry : videoFeed.getEntries())
			{
				// get the media group for the video feed
				YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
				// get the video in the database that is associated with the video id
				Video video = Video.find("byId", mediaGroup.getVideoId()).first();
				// get the video statistics for the video
				YtStatistics stats = videoEntry.getStatistics();
				if(stats != null && video != null ) 
				{
					// set the number of views for the video
					stats.setViewCount(video.getViews());
					// set the number of likes for the video
					stats.setFavoriteCount(video.getLikes());
				}
			}
				
			// return the result
			renderJSON(new JSONObject(videoFeed).toString());
		}
		catch (Exception e)
		{
			// error get the user's videos
			renderJSON(new JSONObject(videoFeed).toString());
		}
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
		// get the video of interest from YouTube
		String videoUrl = "https://gdata.youtube.com/feeds/api/users/default/uploads/" + id;
		VideoEntry videoEntry = null;
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON(new JSONObject(videoEntry).toString());
		}
		try
		{
			videoEntry = AuthenticationService.getYouTubeService().getEntry(new URL(videoUrl), VideoEntry.class);
					
			// Get the database entry for the video to combine with the youtube feed
			if (videoEntry != null)
			{
				// get the media group for the video feed
				YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
				// get the video in the database that is associated with the video id
				Video video = Video.find("byId", mediaGroup.getVideoId()).first();
				// get the video statistics for the video
				YtStatistics stats = videoEntry.getStatistics();
				if(stats != null && video != null ) 
				{
					// set the number of views for the video
					stats.setViewCount(video.getViews());
					// set the number of likes for the video
					stats.setFavoriteCount(video.getLikes());
					renderJSON(new JSONObject(videoEntry).toString());
				}
			}

			renderJSON(new JSONObject(videoEntry).toString());
		}
		catch (Exception e)
		{
			// error interacting with YouTube API
			renderJSON(new JSONObject(videoEntry).toString());
		}
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
		renderJSON("{\"result\": " + numLikes +"}");		
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
		renderJSON("{\"result\": " + numViews +"}");
	}
	
}