package controllers;

import interfaces.YTPlayer;
import models.Video;

/**
 * 
 * @author Cella Sum, Arudra Venkat, Dustin Overmiller, and Justin Kambic
 *
 */
public class VideoChannelController implements YTPlayer{

	Video currentVideo;
	int volume;
	YTPlayer player;
}
