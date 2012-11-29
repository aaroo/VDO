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
    
    public static void myvideos() {
    	render();
    }
    
    public static void editvideo() {
       render();
    }
    
    public static void edituser() {
       render();
    }
    
    public static void users() {
       render();
    }	
	
}