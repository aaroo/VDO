package models;

import java.util.List;
import org.joda.time.DateTime;

/** 
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines the Video Model to store all video related information
 *
 */
public class Video {
	private int id;
	private DateTime uploadDate;
	private String videoType;
	private String owner;
	private String title;
	private String description;
	private List<String> tags;
	private int likes;
	private int views;
	
	/**
	 * Constructor for the Video Class
	 * @param ident
	 * @param upDate
	 * @param vidType
	 * @param own
	 * @param vTitle
	 * @param desc
	 * @param tag
	 * @param numLike
	 * @param numView
	 */
	public Video(int ident, DateTime upDate, String vidType, String own, String vTitle, String desc, List<String> tag, int numLike, int numView)
	{
		id = ident;
		uploadDate = upDate;
		videoType = vidType;
		owner = own;
		title = vTitle;
		description = desc;
		tags = tag;
		likes = numLike;
		views = numView;		
	}
	
	/**
	 * Get the id for the video
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Get the upload date for this video
	 */
	public DateTime getUploadDate()
	{
		return uploadDate;
	}
	
	/**
	 * Get the video type for the video
	 */
	public String getVideoType()
	{
		return videoType;
	}
	
	/**
	 * Get the owner of the video
	 */
	public String getOwner()
	{
		return owner;
	}
	
	/**
	 * Get the Title of the video
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Get the description for the video
	 */
	public String getDesc()
	{
		return description;
	}
	
	/**
	 * Get the Tags for the video
	 */
	public List<String> getTags()
	{
		return tags;
	}
	
	/**
	 * Get the number of Likes for the video
	 */
	public int getLikes()
	{
		return likes;
	}
	
	/**
	 * Set the number of likes for a video
	 */
	public void setLikes(int like)
	{
		likes = like;
	}
	
	/**
	 * Get the number of views for the video
	 */
	public int getViews()
	{
		return views;
	}
	
	/**
	 * Set the number of views for the video
	 */
	public void setViews(int view)
	{
		views = view;
	}
	
}
