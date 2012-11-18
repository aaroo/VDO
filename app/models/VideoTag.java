package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

/** 
 * Tag Class
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 * 
 * This implements the tag references associated with a video
 *
 */
@Entity
public class VideoTag extends Model implements Comparable<VideoTag> {
	
	@Required
	public String name;
	
	/**
	 * Tag(String name)
	 * 
	 * Constructor to create a Tag object
	 * 
	 * @param name
	 */
	private VideoTag(String name) {
		this.name = name;
	}
	
	/**
	 * toString()
	 * 
	 * Convert the object to a string representation
	 * 
	 * @return
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * findOrCreateByName(String name)
	 * 
	 * Locate a given tag or create it if it does not exist
	 * 
	 * @param name
	 * @return
	 */
	public static VideoTag findOrCreateByName(String name) {
		VideoTag tag = VideoTag.find("byName", name).first();
		if (tag == null) {
			tag = new VideoTag(name);
		}
		
		return tag;
	}
	
	/**
	 * compareTo(Tag otherTag)
	 * 
	 * Provide a comparator to compare Two Tags
	 * 
	 */
	public int compareTo(VideoTag otherTag) {
		return name.compareTo(otherTag.name);
	}
}