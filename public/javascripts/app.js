var player;
var App = Em.Application.create({
	ready: function() {	
		if(window.location.href.indexOf("/myvideos") > -1) {
			App.videolistController.getVideolist(); 
		} else if(window.location.href.indexOf("/editvideo") > -1) {
			App.editVideoController.getVideoInfo(window.location.href.substring(window.location.href.lastIndexOf("/") + 1));
		} else if(window.location.href.indexOf("/edituser") > -1) {
			App.editUserController.getUserInfo("vdo575");
		} else if(window.location.href.indexOf("/users") > -1) {
		} else {
			
		}
		var tag = document.createElement('script');
		tag.src = "//www.youtube.com/iframe_api";
		var firstScriptTag = document.getElementsByTagName('script')[0];
		firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);	
	}
});


/* Models */
App.Video = Em.Object.extend({
	authorName:		null,
	rating:			null,
	id: 				null,
	title:			null,
	category: 		null,
	description:	null,
	keywords:		null,
	thumbnail:		null,
	duration:		null,
	published:		null,
	favoriteCount:	null,
	viewCount:		null,
	likesCount:		null,
	authorDate: function() {
    return 'Uploaded by ' + this.get('author') + ' on ' + this.get('published');
  }.property(),
  editLink: function() {
	  return '/application/editvideo/' + this.get('id');
  }.property('id')
});

App.User = Em.Object.extend({
	username:	null,
	email:		null,
	firstName:	null,
	lastName:	null,
	role:			null,
	joinDate:	null,
	lastLogin:	null,
	editLink: function() {
	  return '/application/edituser/' + this.get('username');
  }.property('username')
});

App.Category = Em.Object.extend({
	term:				null,
	label:			null,
	isSelectedCategory: function() {
		return this.get('term') === App.editVideoController.video.category;
	}.property('term')
});

App.Role = Em.Object.extend({
	term:				null,
	label:			null,
	isSelectedRole: function() {
		return this.get('term') === App.editUserController.user.role;
	}.property('term')
});

/* Views */
App.EditVideoView = Ember.View.extend({
  id: null,
  title: null,
  description: null,
  category: null,
  url: function() {
	  return 'http://www.youtube.com/embed/' + this.get('id');
  }.property('id')
});

App.EditUserView = Ember.View.extend({
	username: null,
	email: null,
	firstName: null,
	lastName: null,
	role: null
});

App.LoginFormView = Ember.View.extend({
	email:			null,
	password:		null,
	handleLogin: function() {
		if(this.get('email') === "vdo575@gmail.com" && this.get('password') === "vvddoo575") {
			window.location = "/application/myvideos";
		}
	}
});

/* Controllers */
App.currentVideoController = Ember.Object.create({
	video: null
});

App.editVideoController = Ember.Object.create({
	video: null,
	getVideoInfo: function(id) {
		var url="https://gdata.youtube.com/feeds/api/videos/" + id + "?v=2&alt=json";
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			success:function(data) {
				
				var item = data.entry;
				
				var date = new Date(item.published.$t);
				var prettyDate = date.toDateString();
				
				var video = App.Video.create({
						author:			item.author[0].name.$t,
						rating:			item.gd$rating.average,
						id:				id,
						title:			item.title.$t,
						category: 		item.media$group.media$category[0].$t,
						description:	item.media$group.media$description.$t,
						keywords:		item.media$group.media$keywords.$t,
						thumbnail:		item.media$group.media$thumbnail[0].url,
						duration:		item.media$group.yt$duration.seconds,
						published:		prettyDate,
						favoriteCount:	item.yt$statistics.favoriteCount,
						viewCount:		item.yt$statistics.viewCount
				});
				App.editVideoController.set('video', video);
				App.categoryController.getCategories(); 
			}
		});
	}
});

App.editUserController = Ember.Object.create({
	user: null,
	getUserInfo: function(username) {
		var user = App.User.create ({
			username: username,
			firstName: 'Cella',
			lastName: 'Sum',
			email: 'cella.sum@gmail.com',
			role: 'curator'
		});
		App.editUserController.set('user', user);
	}
});

App.playlistController = Ember.ArrayController.create({
	content:[],
	getPlaylist: function() {
		var me = this;
		var url="https://gdata.youtube.com/feeds/api/users/BeachHouseVideoZone/uploads?alt=json";
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			success:function(data) {
				me.set('content',[]);
				
				var idList = new Array();
				
				$.each(data.feed.entry, function(i, item) {
					
					//var date = new Date(item.media$group.yt$uploaded.$t)
					var date = new Date(item.published.$t);
					var prettyDate = date.toDateString();
					
					var id = item.id.$t;
					id= id.substr(id.lastIndexOf("/") + 1);
					
					var video = App.Video.create({
						author:			item.author[0].name.$t,
						rating:			item.gd$rating.average,
						id:				id,
						title:			item.title.$t,
						category: 		item.media$group.media$category[0].$t,
						description:	item.media$group.media$description.$t,
						keywords:		item.media$group.media$keywords.$t,
						thumbnail:		item.media$group.media$thumbnail[0].url,
						duration:		item.media$group.yt$duration.seconds,
						published:		prettyDate,
						favoriteCount:	item.yt$statistics.favoriteCount,
						viewCount:		item.yt$statistics.viewCount
					});
					
					idList.push(video.id);
					me.pushObject(video);
					
					
				});
				App.playlistController.addPlaylist(idList);
					App.playlistController.adjustVideoListWidth(data.feed.entry.length);
					App.playlistController.addPopovers();
				
			},
			error:function(jqXHR, exception) {
				alert("generatePlaylist ERROR");
			} 
		});
	},
	playVideoAt: function(event) {
		var index = $('a.thumbnail').index($(event.target).parent());
		player.playVideoAt(index);
	},
	playNextVideo: function(event) {
		player.nextVideo();
	},
	playPrevVideo: function(event) {
		player.previousVideo();
	},
	addPlaylist: function(idList) {
		var idString = "";
		$.each(idList, function(key, value) { 
			if(key == 0) {
			} else if (key < idList.length - 1) {
				idString = idString + value + ",";
			} else {
				idString = idString + value;
			}
		});
			player = new YT.Player('player', {
			          height: '390',
			          width: '640',
			          playerVars: { 'autoplay': 1, 'controls': 0, 'modestbranding': 1, 'rel':0, 'showinfo':0, 'playlist': idString },
			          videoId: idList[0],
			          events: {
			            'onReady': App.playlistController.onPlayerReady,
			            'onStateChange': App.playlistController.onPlayerStateChange,
			            'onError': App.playlistController.onPlayerError
			          }
			});
			
		
	},
	onPlayerReady: function(event) {
		
	},
	onPlayerStateChange: function(event) {
		if(event.data == 1) {
			// video is playing
			App.currentVideoController.set('video', App.playlistController.objectAt(player.getPlaylistIndex()));
			var widthQuery = window.matchMedia("(min-width: 978px)");
			if(widthQuery.matches) {
				$("#video-list").stop().animate({scrollLeft:300*player.getPlaylistIndex()});
			}
			
		}
	},
	onPlayerError: function(event) {
		console.log("onPlayerError:" + event.data);
	},
	addPopovers: function() {
		$('.thumbnails').popover({
			selector: "a[rel=popover]",
			trigger: 'hover',
			placement: 'top'
		})
		
		$('.videoWrapper').hover(
		  function () {
		  	 $('#video-nav').fadeIn();
		  	  $('#video-info').fadeIn();
		  },
		  function () {
		  	  $('#video-nav').fadeOut();
		  	   $('#video-info').fadeOut();
		  }
	  ); 
	},
	adjustVideoListWidth: function(num) {
		var widthQuery = window.matchMedia("(min-width: 978px)");
	
		if(widthQuery.matches) {
			$("body").css('width', $(window).width() + "px");
			$("body").css('height', $(window).height() + "px");
			$(".thumbnails").css('width',num*300 + 'px');
		} else {
			$(".thumbnails").css('width','auto');
			$("body").css('width', 'auto');
			$("body").css('height', 'auto');
		}
		
		$(window).resize(function() {
			var widthQuery = window.matchMedia("(min-width: 978px)");
			
			if(widthQuery.matches) {
				$(".thumbnails").css('width',num*300 + 'px');
				$("body").css('width', $("window").width() + "px");
				$("body").css('height', $("window").height() + "px");
			} else {
				$(".thumbnails").css('width','auto');
				$("body").css('width', 'auto');
				$("body").css('height', 'auto');
			}
		});
	 	$("#video-list, .thumbnail, .popover").hover(
		  function () {
		  	 var widthQuery = window.matchMedia("(min-width: 978px)");
		  	 if(widthQuery.matches) {
		  	 
		  	 	$("#video-list").stop().animate({ 
						marginBottom: "0px"
						}, 500 );
		    }
		  },
		  function () {
		  	 var widthQuery = window.matchMedia("(min-width: 978px)");
		  	 if(widthQuery.matches) {
		    	$("#video-list").stop().animate({ 
						marginBottom: "-265px"
						}, 500 );
	
		    }
		  }
	  );
	}
});

App.videolistController = Ember.ArrayController.create({
	content:[],
	getVideolist: function(type) {
		var me = this;
		var url="https://gdata.youtube.com/feeds/api/users/BeachHouseVideoZone/uploads?alt=json";
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			success:function(data) {
				me.set('content',[]);
				
				var idList = new Array();
				
				$.each(data.feed.entry, function(i, item) {
				
					var date = new Date(item.published.$t);
					var prettyDate = date.toDateString();
					
					var id = item.id.$t;
					id= id.substr(id.lastIndexOf("/")+1);
					
					
					var video = App.Video.create({
						author:			item.author[0].name.$t,
						rating:			item.gd$rating.average,
						id:				id,
						title:			item.title.$t,
						category: 		item.media$group.media$category[0].$t,
						description:	item.media$group.media$description.$t,
						keywords:		item.media$group.media$keywords.$t,
						thumbnail:		item.media$group.media$thumbnail[0].url,
						duration:		item.media$group.yt$duration.seconds,
						published:		prettyDate,
						favoriteCount:	item.yt$statistics.favoriteCount,
						viewCount:		item.yt$statistics.viewCount
					});
					
					idList.push(video.id);
					me.pushObject(video);
				});
			}
		});
	}
});

App.userlistController = Ember.ArrayController.create({
		content: [
			App.User.create({
				username:	'vdo575',
				email:		'cella.sum@gmail.com',
				firstName:	'Cella',
				lastName:	'Sum',
				role:			'curator',
				joinDate:	'11/24/12',
				loginDate:	'11/24/12'
			}),
			
			App.User.create({
				username:	'overdc',
				email:		'overdc@udel.edu',
				firstName:	'Dustin',
				lastName:	'Overmiller',
				role:			'curator',
				joinDate:	'11/24/12',
				loginDate:	'11/24/12'
			}),
			
			App.User.create({
				username:	'jek5104',
				email:		'jek5104@gmail.com',
				firstName:	'Justin',
				lastName:	'Kambic',
				role:			'curator',
				joinDate:	'11/24/12',
				loginDate:	'11/24/12'
			}),
			
			App.User.create({
				username:	'arudravenkat',
				email:		'arudravenkat@gmail.com',
				firstName:	'Arudra',
				lastName:	'Venkat',
				role:			'admin',
				joinDate:	'11/24/12',
				loginDate:	'11/24/12'
			})
			
			
		]
});

App.categoryController = Ember.ArrayController.create({
	content:[],
	getCategories: function() {
		var me = this;
		var url ="http://gdata.youtube.com/schemas/2007/categories.cat";
		$.ajax({
			url: url,
			type: "GET",
			dataType: "xml",
			success:function(data) {
				me.set('content',[]);
				
				$(data).find('category').each(function(){
					var category = App.Category.create({
						term:	$(this).attr('term'),
						label: $(this).attr('label')
					});
				
					me.pushObject(category);
				});
			}
		});
	}
});


App.roleController = Ember.ArrayController.create({
	content: [
  App.Role.create({
  		term: 'curator',
  		label: 'Curator'
  }),
  App.Role.create({
  		term: 'admin',
  		label: 'Administrator'
  })]
});

function onYouTubeIframeAPIReady() {	
		App.playlistController.getPlaylist();  
}

function getParameterByName(name)
{
  name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
  var regexS = "[\\?&]" + name + "=([^&#]*)";
  var regex = new RegExp(regexS);
  var results = regex.exec(window.location.search);
  if(results == null)
    return "";
  else
    return decodeURIComponent(results[1].replace(/\+/g, " "));
}
