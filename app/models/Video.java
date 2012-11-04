package models;

import java.util.List;
import org.joda.time.DateTime;
import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import play.data.validation.*;

/** 
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This defines the Video Model to store all video related information
 *
 */
 @Entity
public class Video extends Model {
	@Required
	private int id;
	@Required
	private DateTime uploadDate;
	@Required
	private String owner;
	@Required
	private String title;
	@Required
	private String description;
	@Required
	private List<String> tags;
	@Required
	private int likes;
	@Required
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
	public Video(int ident, DateTime upDate, String own, String vTitle, String desc, List<String> tag, int numLike, int numView)
	{
		id = ident;
		uploadDate = upDate;
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
	 * Set the upload date for this video
	 */
	public void setUploadDate(DateTime upDate)
	{
		uploadDate = upDate;
	}
	
	/**
	 * Get the owner of the video
	 */
	public String getOwner()
	{
		return owner;
	}
	
	/**
	 * Set the owner of the video
	 */
	public void setOwner(String Owner)
	{
		owner = Owner;
	}
	
	/**
	 * Get the Title of the video
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Set the Title of the video
	 */
	public void setTitle(String Title)
	{
		title = Title;
	}
	
	/**
	 * Get the description for the video
	 */
	public String getDesc()
	{
		return description;
	}
	
	/**
	 * Set the description for the video
	 */
	public void setDesc(String desc)
	{
		description = desc;
	}
	
	/**
	 * Get the Tags for the video
	 */
	public List<String> getTags()
	{
		return tags;
	}
	
	/**
	 * Set the tags for the video
	 */
	public void setTags(List<String> tagList)
	{
		tags = tagList;
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
