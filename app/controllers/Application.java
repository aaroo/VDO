package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

/**
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 *
 */
public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render("Your new application is ready."));
    
  }
  
}