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
import javax.activation.MimetypesFileTypeMap;


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
		String videoFeedUrl = "http://gdata.youtube.com/feeds/api/videos";
		// setup the initial query
		VideoFeed videoFeed = null;
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON(getVideoFeedJSON(videoFeed));
		}
		try
		{
			YouTubeQuery query = new YouTubeQuery(new URL(videoFeedUrl));
			query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
			// set to filter just the programs videos
			query.setAuthor("vdo575");
			// set to order by published videos
			query.setOrderBy(YouTubeQuery.OrderBy.PUBLISHED);
			// set it to grab just the user's videos if set otherwise ignore it and grab all videos
			if (owner != null && !owner.equals("") && !owner.equals(" "))
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
				YtStatistics stats = videoEntry.getStatistics();
				// get the video in the database that is associated with the video id
				int numViews = 0;
				int numLikes = 0;
				try
				{
					Video video = Video.find("byVidId", mediaGroup.getVideoId()).first();
					numViews = video.getViews();
					numLikes = video.getLikes();
				}
				catch (Exception e)
				{
					/* could not find the video file */
				}
				// get the video statistics for the video
				if(stats != null) 
				{
					// set the number of views for the video
					stats.setViewCount(numViews);
					// set the number of likes for the video
					stats.setFavoriteCount(numLikes);	
				}
			}
			
			// return the result
			renderJSON(getVideoFeedJSON(videoFeed));
		}
		catch (Exception e)
		{
			// error getting the user's videos
			renderJSON(getVideoFeedJSON(videoFeed));
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
			renderJSON("{\"result\":" + false +"}");
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
				renderJSON("{\"result\":" + false +"}");
			}
			
			try 
			{
				// delete it from the database
				Video delVideo = Video.find("byVidId", id).first();
				
				// found the video in the database, or an exception is thrown and false is returned
				delVideo.delete();
			}
			catch(Exception e)
			{
				// means video is not in the database so return true as if it was deleted
				renderJSON("{\"result\":" + true +"}");
			}
			// return success
			renderJSON("{\"result\":" + true +"}");
		}
		catch (Exception e)
		{
			// error interacting with YouTube API
			renderJSON("{\"result\":" + false +"}");
		}
	}
	
	/**
	 * addVideo(String filename, String title, String description, List<String> tagList, String category)
	 * 
	 * Upload the video and metadata to the Youtube as well as upload metadata to the database
	 * 
	 * @param filename
	 * @param title
	 * @param description
	 * @param tagList
	 * @param category
	 */
	public static void addVideo(String filename, String title, String description, List<String> tagList, String category)
	{
		String videoId = "";
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON("{\"id\":\"" + videoId +"\"}");
		}
		List<VideoTag> tags = new ArrayList<VideoTag>();
		for (int iI = 0; iI < tagList.size(); iI++)
		{
			tags.add(new VideoTag(tagList.get(iI)));
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
	//	newEntry.setLocation("Philadelphia, PA");
		
		// set the media file source
		MimetypesFileTypeMap mediaTypes = new MimetypesFileTypeMap();
	    mediaTypes.addMimeTypes("video/quicktime qt mov moov");
	    mediaTypes.addMimeTypes("video/mpeg mpeg mpg mpe mpv vbs mpegv");
	    mediaTypes.addMimeTypes("video/msvideo avi");
		String mimeType = mediaTypes.getContentType(new File(filename));
		MediaFileSource ms = new MediaFileSource(new File(filename), mimeType);
		newEntry.setMediaSource(ms);
		
		// try uploading the video to YouTube
		try
		{
			// try uploading the video metadata to Youtube and get the token
			URL uploadUrl = new URL("http://uploads.gdata.youtube.com/feeds/api/users/default/uploads");
			VideoEntry createdEntry = AuthenticationService.getYouTubeService().insert(uploadUrl, newEntry);
			if (createdEntry.getMediaGroup().getVideoId().length() > 0)
			{
				videoId = createdEntry.getMediaGroup().getVideoId();
				// add the video to the database
				Video video = new Video(videoId, new Date(), AuthenticationService.getCurrentUser().getUserName(), title, description, category, tags, 0, 0).save();
				// return the video id
				renderJSON("{\"id\":\"" + videoId +"\"}");
			}

		}
		catch (Exception e)
		{
			// error uploading the video
			renderJSON("{\"id\":\"" + videoId +"\"}");
		}
		
		// failed to upload video
		renderJSON("{\"id\":\"" + videoId +"\"}");
	}
	

	/**
	 * updateVideo(String title, String description, List<String> tags, String category)
	 * 
	 * Update the video metadata within Youtube
	 * 
	 * @param title
	 * @param description
	 * @param tagList
	 * @param category
	 */
	public static void updateVideo(String id, String title, String description, List<String> tagList, String category)
	{
		// get the video feed to update the metadata in YouTube
		String videoUrl = "https://gdata.youtube.com/feeds/api/users/default/uploads/" + id;
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON("{\"result\":" + false + ",\"status\":\"youtube api not open\"}");
		}
		List<VideoTag> tags = new ArrayList<VideoTag>();
		for (int iI = 0; iI < tagList.size(); iI++)
		{
			tags.add(new VideoTag(tagList.get(iI)));
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
				renderJSON("{\"result\":" + false + ",\"status\":\"video not found\"}");
			}
			
			// Update the database with this data just to be accurate
			Video video = Video.find("byVidId", id).first();	// an exception will be thrown if the video is not found
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
			// result was successful
			renderJSON("{\"result\":" + true +"}");
		}
		catch (Exception e)
		{
			// error interacting with YouTube API
			renderJSON("{\"result\":" + false +",\"status\":" + e + "}");
		}
	}
	
	/**
	 * searchVideos(List<String> tags, String owner)
	 * 
	 * take the list of tags and the owner and fetch all relating videos
	 * 
	 * @param tagList
	 * @param owner
	 */
	public static void searchVideos(List<String> tagList, String owner)
	{
		// get all of the videos and metadata from Youtube
		String videoFeedUrl = "https://gdata.youtube.com/feeds/api/videos";
		// Initialize the return structure
		VideoFeed videoFeed = null;
		List<VideoTag> tags = new ArrayList<VideoTag>();
		for (int iI = 0; iI < tagList.size(); iI++)
		{
			tags.add(new VideoTag(tagList.get(iI)));
		}
		// setup the initial query
		if (AuthenticationService.getYouTubeService() == null)
		{
			// youtube api is not open
			renderJSON(getVideoFeedJSON(videoFeed));
		}
		try
		{
			YouTubeQuery query = new YouTubeQuery(new URL(videoFeedUrl));
			// set to filter just the programs videos
			query.setAuthor("vdo575");
			// set to order by published videos
			query.setOrderBy(YouTubeQuery.OrderBy.PUBLISHED);
			// add the developer tag to grab just the owner videos
			if (owner != null && !owner.equals("") && !owner.equals(" "))
			{
				CategoryFilter categoryFilter = new CategoryFilter();
				categoryFilter.addCategory(new Category(YouTubeNamespace.DEVELOPER_TAG_SCHEME, owner));
				query.addCategoryFilter(categoryFilter);
			}
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
				YtStatistics stats = videoEntry.getStatistics();
				// get the video in the database that is associated with the video id
				int numViews = 0;
				int numLikes = 0;
				try
				{
					Video video = Video.find("byVidId", mediaGroup.getVideoId()).first();
					numViews = video.getViews();
					numLikes = video.getLikes();
				}
				catch (Exception e)
				{
					/* could not find the video file */
				}
				// get the video statistics for the video
				if(stats != null) 
				{
					// set the number of views for the video
					stats.setViewCount(numViews);
					// set the number of likes for the video
					stats.setFavoriteCount(numLikes);	
				}
			}
				
			// return the result
			renderJSON(getVideoFeedJSON(videoFeed));
		}
		catch (Exception e)
		{
			// error get the user's videos
			renderJSON(getVideoFeedJSON(videoFeed));
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
			renderJSON(getVideoEntryJSON(videoEntry));
		}
		try
		{
			videoEntry = AuthenticationService.getYouTubeService().getEntry(new URL(videoUrl), VideoEntry.class);
					
			// Get the database entry for the video to combine with the youtube feed
			if (videoEntry != null)
			{
				// get the media group for the video feed
				YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
				YtStatistics stats = videoEntry.getStatistics();
				// get the video in the database that is associated with the video id
				int numViews = 0;
				int numLikes = 0;
				try
				{
					Video video = Video.find("byVidId", mediaGroup.getVideoId()).first();
					numViews = video.getViews();
					numLikes = video.getLikes();
				}
				catch (Exception e)
				{
					/* could not find the video file */
				}
				// get the video statistics for the video
				if(stats != null) 
				{
					// set the number of views for the video
					stats.setViewCount(numViews);
					// set the number of likes for the video
					stats.setFavoriteCount(numLikes);	
				}
			}

			renderJSON(getVideoEntryJSON(videoEntry));
		}
		catch (Exception e)
		{
			// error interacting with YouTube API
			renderJSON(getVideoEntryJSON(videoEntry));
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
		int numLikes = 0;
		// get the video from the database
		try {
			Video vid = Video.find("byVidId", id).first();
		
			// add one to the number of likes
			vid.setLikes(vid.getLikes() + 1);
			
			// save the update to the database
			vid.save();
			
			// set the number of likes to be returned
			numLikes = vid.getLikes();
			
			// return number of likes
			renderJSON("{\"result\":" + numLikes +"}");		
		}
		catch (Exception e)
		{
			// return number of likes
			renderJSON("{\"result\":" + numLikes +"}");		
		}	
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
		int numViews = 0;
		// get the video from the database
		try
		{
			Video vid = Video.find("byVidId", id).first();

			// add one to the number of likes
			vid.setViews(vid.getViews() + 1);
		
			// save the update to the database
			vid.save();
			
			// set the number of views to be returned
			numViews = vid.getViews();
			
			// return number of views
			renderJSON("{\"result\":" + numViews +"}");
		}
		catch (Exception e)
		{
			// return number of views
			renderJSON("{\"result\":" + numViews +"}");
		}
	}
	
	private static String getVideoFeedJSON(VideoFeed videoFeed)
	{
		String jsonString = "{\"entries\":[";
		if (videoFeed != null)
		{
			for (int i = 0; i < videoFeed.getEntries().size(); i++)
			{
				VideoEntry videoEntry = videoFeed.getEntries().get(i);
				jsonString += VideoIntegrationService.getVideoEntryJSON(videoEntry);
				if (i < (videoFeed.getEntries().size()-1))
				{
					jsonString += ", ";
				}
			}
		}
		jsonString += "]}";
		
		return jsonString;
	}
	
	private static String getVideoEntryJSON(VideoEntry videoEntry)
	{
		String jsonString = "{";
		if (videoEntry != null)
		{
			// add in the author
			jsonString += "\"Author\":\"";
			for (MediaCategory cat : videoEntry.getMediaGroup().getCategories())
			{
				if (cat.getScheme().contains("developertags.cat"))
				{
					jsonString += cat.getContent();
				}
			}
			jsonString += "\", ";
			
			// add in the videoID
			jsonString += "\"VideoID\":\"" + videoEntry.getMediaGroup().getVideoId() + "\", ";
			
			// add in the video title
			jsonString += "\"Title\":\"" + videoEntry.getMediaGroup().getTitle().getPlainTextContent() + "\", ";
			
			// add in the category
			jsonString += "\"Category\":\"";
			for (MediaCategory cat : videoEntry.getMediaGroup().getCategories())
			{
				if (cat.getScheme().contains("categories.cat"))
				{
					jsonString += cat.getContent();
				}
			}
			jsonString += "\", ";
			
			// add in the description
			jsonString += "\"Description\":\"" + videoEntry.getMediaGroup().getDescription().getPlainTextContent() + "\", ";
			
			// add in the keywords
			jsonString += "\"Keywords\":[";
			for (int i = 0; i < videoEntry.getMediaGroup().getKeywords().getKeywords().size(); i++)
			{
				String key = videoEntry.getMediaGroup().getKeywords().getKeywords().get(i);
				jsonString += "\"" + key + "\"";
				if (i < (videoEntry.getMediaGroup().getKeywords().getKeywords().size()-1))
				{
					jsonString += ", ";
				}
			}
			jsonString += "], ";
			
			// add in the thumbnails
			jsonString += "\"Thumbnails\":[";
			for (int i = 0; i < videoEntry.getMediaGroup().getThumbnails().size(); i++)
			{
				MediaThumbnail thumb = videoEntry.getMediaGroup().getThumbnails().get(i);
				jsonString += "{\"url\":\"" + thumb.getUrl() + "\"}";
				if (i < (videoEntry.getMediaGroup().getThumbnails().size()-1))
				{
					jsonString += ", ";
				}
			}
			jsonString += "], ";
			
			// add in the duration
			jsonString += "\"Duration\":" + videoEntry.getMediaGroup().getDuration() + ", ";
			
			// add in the published date
			jsonString += "\"Published\":\"" + videoEntry.getPublished().toUiString() + "\", ";
			
			// add in view count
			long views = 0;
			long likes = 0;
			YtStatistics stats = videoEntry.getStatistics();
			if (stats != null)
			{
				views = stats.getViewCount();
				likes = stats.getFavoriteCount();
			}
			
			// add in the view count
			jsonString += "\"Views\":" + views + ", ";
			
			// add in the like count
			jsonString += "\"Likes\":" + likes;
			
			
		}
		jsonString += "}";
		
		return jsonString;
	}
}