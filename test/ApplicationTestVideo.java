import javax.xml.ws.Response;

import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import org.junit.*;
import play.libs.*;
import org.junit.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import models.*;
 
public class ApplicationTestVideo extends FunctionalTest {
     
	/* functional test to get the video feed for all videos and for an owner */
	@Test (timeout=1000000)
	public void testVideoService() {
		/* connect to the YouTubeAPI */
		String videoId = "07hu0oAiBCM";
		JSONObject json;
		Http.Response response = GET("/authenticationservice/openyoutubeapi");
		String resp = getContent(response).toString();
        assertTrue(resp.equals("{\"result\":true}"));
		response = GET("/authenticationservice/signout");
		/* sign up a user */
		Map<String, String> signUpParams = new HashMap<String, String>();
		signUpParams.put("username", "dco15");
	    signUpParams.put("password", "password");
	    signUpParams.put("email", "dco15@drexel.edu");
	    response = POST("/authenticationservice/signup", signUpParams);
	    resp = getContent(response).toString();
        assertTrue(resp.equals("{\"result\":true}"));
		/* need to add a video to youtube for a user first */
        /*
        Map<String, String> addVideoParams = new HashMap<String, String>();
        addVideoParams.put("filename", "./test/Francesca Jane.mp4");
        addVideoParams.put("title", "CS575-Test1");
        addVideoParams.put("description", "This is a video uploaded as a test.");
        addVideoParams.put("tagList[0]", "cs575");
        addVideoParams.put("tagList[1]", "teamVDO");
        addVideoParams.put("category", "Tech");
        response = POST("/videointegrationservice/addvideo", addVideoParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	videoId = json.getString("id");
	    	assertTrue(videoId.length() > 0);
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    */
	    /* get the video feed */
        /*
	    Map<String, String> getVideoParams = new HashMap<String, String>();
	    getVideoParams.put("owner", "dco15");
	    response = POST("/videointegrationservice/getvideos", getVideoParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	assertTrue(json.getJSONArray("entries").length() > 0);
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    */
	    /* get the video feed */
        /*
	    getVideoParams = new HashMap<String, String>();
	    getVideoParams.put("owner", "dco15");
	    response = POST("/videointegrationservice/getvideos", getVideoParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	assertTrue(json.getJSONObject("feed").getJSONArray("entry").length() > 0);
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    */
	    /* test updating the video */
        /*
	    Map<String, String> updateVideoParams = new HashMap<String, String>();
	    updateVideoParams.put("id", videoId);
	    updateVideoParams.put("title", "My New Movie");
	    updateVideoParams.put("description", "This is a new video uploaded as a test.");
	    updateVideoParams.put("tagList[0]", "test1");
	    updateVideoParams.put("tagList[1]", "new-cs575");
	    updateVideoParams.put("tagList[2]", "TeamVDO");
	    updateVideoParams.put("category", "Tech");
	    response = POST("/videointegrationservice/updatevideo", updateVideoParams);
	    resp = getContent(response).toString();
        assertTrue(resp.equals("{\"result\":true}"));
        */
        /* search video */
        /*
	    Map<String, String> searchVideoParams = new HashMap<String, String>();
	    searchVideoParams.put("tagList[0]", "teamVDO");
	    searchVideoParams.put("owner", "dco15");
	    response = POST("/videointegrationservice/searchvideos", searchVideoParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	assertTrue(json.getJSONArray("entries").length() > 0);
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    */
	    /* search video */
        /*
	    searchVideoParams = new HashMap<String, String>();
	    searchVideoParams.put("tagList", "test1");
	    searchVideoParams.put("owner", "dco15");
	    response = POST("/videointegrationservice/searchvideos", searchVideoParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	assertTrue(json.getJSONObject("feed").getJSONArray("entry").length() > 0);
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    */
	    /* get video info */
        
	    Map<String, String> getVideoIdParams = new HashMap<String, String>();
	    getVideoIdParams.put("id", videoId);
	    response = POST("/videointegrationservice/getvideoinfo", getVideoIdParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	System.out.println(json.getString("VideoID"));
	    	assertTrue(json.getString("VideoID").equals(videoId));
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    
	    /* like video */
        /*
	    Map<String, String> likeVideoIdParams = new HashMap<String, String>();
	    likeVideoIdParams.put("id", videoId);
	    response = POST("/videointegrationservice/likevideo", likeVideoIdParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	assertTrue(json.getInt("result") > 0);
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    */
	    /* view video */
        /*
	    Map<String, String> viewVideoIdParams = new HashMap<String, String>();
	    viewVideoIdParams.put("id", videoId);
	    response = POST("/videointegrationservice/viewvideo", viewVideoIdParams);
	    try
	    {
	    	json = new JSONObject(getContent(response).toString());
	    	assertTrue(json.getInt("result") > 0);
	    }
	    catch (Exception e)
	    {
	    	assertTrue(false);
	    }
	    */
	    /* delete video */
        /*
	    Map<String, String> deleteParams = new HashMap<String, String>();
	    deleteParams.put("id", videoId);
	    response = POST("/videointegrationservice/deletevideo", deleteParams);
	    resp = getContent(response).toString();
        assertTrue(resp.equals("{\"result\":true}"));
        response = GET("/authenticationservice/signout");
        */
	    
	}
}