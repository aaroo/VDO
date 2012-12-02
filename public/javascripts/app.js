var player;
var App = Em.Application.create({
	ready: function() {	
		if(window.location.href.indexOf("/videos") > -1) {	
			App.currentUserController.getCurrentUserInfo('videos');
		} else if(window.location.href.indexOf("/editvideo") > -1) {App.editVideoController.getVideoInfo(window.location.href.substring(window.location.href.lastIndexOf("/") + 1));
		} else if(window.location.href.indexOf("/edituser") > -1) {
			App.editUserController.getUserInfo(window.location.href.substring(window.location.href.lastIndexOf("/") + 1));
		} else if(window.location.href.indexOf("/users") > -1) {
		} else if(window.location.href.indexOf("/addvideo") > -1) {
			App.categoryController.getCategories();
		} else {
			App.currentUserController.getCurrentUserInfo();
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
	id: 				null,
	title:			null,
	category: 		null,
	description:	null,
	keywords:		null,
	thumbnail:		null,
	duration:		null,
	published:		null,
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
	isAdmin:	null,
	joinDate:	null,
	lastLogin:	null,
	id:			null,
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
  keywords: null,
  url: function() {
	  return 'http://www.youtube.com/embed/' + this.get('id');
  }.property('id'),
  updateVideo: function() {
  
  		// format keywords 
  		var keywords = this.get('keywords');
  		keywords.replace(/\s/g, "");
  		keywords.toLowerCase();
  		var keywordArray = keywords.split(",");
  		
  		var url="/videointegrationservice/updatevideo";
		var params= {
			id: this.get('id'),
			title: this.get('title'),
			description: this.get('description'),
			tagList: keywordArray,
			category: App.categoryController.selected
		}
		
		$.ajax({
				url:url,
				type: "GET",
				data: params,
				dataType: 'json',
				success: function(data) {
					if(data.result == true) {
						window.location = "/application/videos";
					} else {
						alert("an error has occured while updating. please try again.");
					}
				},
				error: function(jqXRH, exception) {
					alert("login ERROR: " + jqXHR.responseText);
				}
		});
  },
  deleteVideo: function() {
	  var url="/videointegrationservice/deletevideo";
		var params= {
			id: this.get('id')
		}
		
		$.ajax({
				url:url,
				type: "GET",
				data: params,
				dataType: 'json',
				success: function(data) {
					if(data.result == true) {
						window.location = "/application/videos";
					} else {
						alert("an error has occured while deleting. please try again.");
					}
				},
				error: function(jqXRH, exception) {
					alert("login ERROR: " + jqXHR.responseText);
				}
		});
  }
});

App.AddVideoView = Ember.View.extend({
  title: null,
  description: null,
  category: null,
  keywords: null,
  file: null,
  status: null,
  addVideo: function() {
  		
  		$(".status-msg").text("loading... (may take a few minutes)");
	  // format keywords 
	  var keywords = this.get('keywords');
	  keywords.replace(/\s/g, "");
	  keywords.toLowerCase();
	  var keywordArray = keywords.split(",");
	  
	  var url="/videointegrationservice/addvideo";
	  var params= {
	  		filename: this.get('file'),
			title: this.get('title'),
			description: this.get('description'),
			tagList: keywordArray,
			category: App.categoryController.selected
		}
		
		
		$.ajax({
				url:url,
				type: "GET",
				data: params,
				dataType: 'json',
				success: function(data) {
					if(data.id != null && data.id != "") {
						$(".status-msg").html('SUCCESS! Your video has been uploaded.  The id number is: ' + data.id + '.  It can take up to 8 hours to show up in your feed, but you can view its details <a href="/application/editvideo/' + data.id + '">here</a>');
						//window.location = "/application/videos";
					} else {
						console.log(data.id);
						alert("an error has occured while attempting to add this video. please try again.");
					}
				},
				error: function(jqXRH, exception) {
					alert("addVideo ERROR: " + jqXHR.responseText);
				}
		});
  }
  
});

App.EditUserView = Ember.View.extend({
	username: null,
	email: null,
	password: null,
	isAdmin: null,
	editUser: function(){		
  		var url="/userintegrationservice/edituser";
		var params= {
			username: this.get('username'),
			password: this.get('password'),
			email: this.get('email'),
			isAdmin: App.roleController.selected
		}
		
		$.ajax({
				url:url,
				type: "GET",
				data: params,
				dataType: 'json',
				success: function(data) {
					if(data.result == true) {
						alert("User Information Successfully Update!");
					} else {
						alert("an error has occured while updating. please try again.");
					}
				},
				error: function(jqXRH, exception) {
					alert("edit user ERROR: " + jqXHR.responseText);
				}
		});		
	},
	
	deleteUser: function() {
	  var url="/userintegrationservice/deleteuser";
		var params= {
			username: this.get('username')
		}
		
		$.ajax({
				url:url,
				type: "GET",
				data: params,
				dataType: 'json',
				success: function(data) {
					if(data.result == true) {
						window.location = "/";
					} else {
						alert("an error has occured while deleting. please try again.");
					}
				},
				error: function(jqXRH, exception) {
					alert("login ERROR: " + jqXHR.responseText);
				}
		});
	}	
});

App.LoginFormView = Ember.View.extend({
	email:			null,
	password:		null,
	handleLogin: function() {
		
		var url="/authenticationservice/login";
		var params = {
				email: this.get('email'),
				password: this.get('password')
		};
		$.ajax({
			url:url,
			type: "GET",
			data: params,
			dataType: 'json',
			success: function(data) {
				if(data.result == true || data.status == "alreadyLoggedIn") {
					window.location = "/application/videos";
				} else {
					alert("your login is not recognized.  please try again or sign up");
				}
			},
			error: function(jqXRH, exception) {
				alert("login ERROR: " + jqXHR.responseText);
			}
		});

	}
});

App.SignupFormView = Ember.View.extend({
	username:		null,
	email:			null,
	password:		null,
	confirmPassword: null,
	handleSignup: function() {
		
		if(this.get('password') === this.get('confirmPassword')) {
			var url="/authenticationservice/signup";
			var params = {
					username: this.get('username'),
					password: this.get('password'),
					email: this.get('email')
			};
			$.ajax({
				url:url,
				type: "GET",
				data: params,
				dataType: 'json',
				success: function(data) {
					if(data.result == true || data.status == "alreadyLoggedIn") {
						window.location = "/application/videos";
					} else {
						alert("an error has occured while signing up. please try again.");
					}
				},
				error: function(jqXRH, exception) {
					alert("login ERROR: " + jqXHR.responseText);
				}
			});
			
		} else {
			alert("the passwords do not match. please try again.");
		}
		
		
	}
});

/* Controllers */
App.currentVideoController = Ember.Object.create({
	video: null
});

App.currentUserController = Ember.Object.create({
	user: null,
	getCurrentUserInfo: function(origin) {
		var url = "/authenticationservice/getcurrentuserinfo";
		
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			success:function(data) {
				var user = App.User.create({
					username: data.username,
					email: data.email,
					isAdmin: data.isAdmin,
					joinDate: data.joinDate,
					lastLogin: data.lastLogin
				});
				
				App.currentUserController.set('user', user);
				
				if(origin == "videos") {
					App.videolistController.getVideolist(); 
				}
			},
			error:function(jqXHR, exception) {
				alert("handleLogout ERROR" + jqXHR.responseText);
			}
		});

	}
});


App.editVideoController = Ember.Object.create({
	video: null,
	getVideoInfo: function(id) {
		var timestamp = "timestamp=" + new Date().getTime();
		var url="/videointegrationservice/getvideoinfo?" + timestamp;
		var params={
			id: id
		}
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			data:params,
			success:function(data) {
				if(!$.isEmptyObject(data)) {
					var video = App.Video.create({
						author:			data.Author,
						id:				data.VideoID,
						title:			data.Title,
						category: 		data.Category.toString(),
						description:	data.Description,
						keywords:		data.Keywords.toString(),
						thumbnail:		data.Thumbnails[0].url,
						duration:		data.Duration,
						published:		data.Published,
						viewCount:		data.Views,
						likesCount:		data.Likes
					});
					App.editVideoController.set('video', video);
					App.categoryController.getCategories(video.category); 

				} else {
					alert("Oops this video appears to no longer exist");
				}
								
				
			}
		});
	}
});

App.editUserController = Ember.Object.create({
	user: null,
	getUserInfo: function(username) {
		var url = "/userintegrationservice/getuserinfo";
		var params = {username: username}
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			data: params,
			success:function(data) {
				if(!$.isEmptyObject(data)) {
					var user = App.User.create({
						username:		data.username,
						email:			data.email,
						password:		data.password,
						isAdmin: 		data.isAdmin,
						joinDate:		data.joinDate,
						lastLogin:		lastLogin,
						id:				data.id,
					});
					App.editUserController.set('user', user);
				}
			},
			error:function(jqXHR, exception) {
				alert("openYoutubeAPI ERROR" + jqXHR.responseText);
			}
		});
	}
});

App.authenticationController = Ember.Object.create({
	openYoutubeAPI: function(origin) {
		var url = "/authenticationservice/openyoutubeapi";
		
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			success:function(data) {
				if (data.result == true) {
					if(origin == "playlist") {
						App.playlistController.getPlaylist();  
					}
				}
			},
			error:function(jqXHR, exception) {
				alert("openYoutubeAPI ERROR" + jqXHR.responseText);
			}
		});
	},
	handleLogout: function() {
		var url = "/authenticationservice/signOut";
		
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			success:function(data) {
				if (data.result == true) {
					window.location = "/application/index";
				}
			},
			error:function(jqXHR, exception) {
				alert("handleLogout ERROR" + jqXHR.responseText);
			}
		});
	}
});

App.playlistController = Ember.ArrayController.create({
	content:[],
	getPlaylist: function() {
		var me = this;
		var url = '/videointegrationservice/getvideos';
		
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			success:function(data) {
				me.set('content',[]);
				
				var idList = new Array();
				
				$.each(data.entries, function(i, item) {
					
					var video = App.Video.create({
						author:			item.Author,
						id:				item.VideoID,
						title:			item.Title,
						category: 		item.Category,
						description:	item.Description,
						keywords:		item.keywords,
						thumbnail:		item.Thumbnails[0].url,
						duration:		item.Duration,
						published:		item.Published
					});
					
					idList.push(video.id);
					me.pushObject(video);
					
					
				});
				App.playlistController.addPlaylist(idList);
					App.playlistController.adjustVideoListWidth(data.entries.length);
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
			
			var videoIndex = player.getPlaylistIndex();
			if(videoIndex == -1) {
				videoIndex = 0;
			}
			App.currentVideoController.set('video', App.playlistController.objectAt(videoIndex));
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
	getVideolist: function() {
		var me = this;
		var timestamp = "timestamp=" + new Date().getTime();
		var url = '/videointegrationservice/getvideos?' + timestamp;
		var params = {};
		if(App.currentUserController.user.isAdmin == false) {
			params = { owner: App.currentUserController.user.username };
		}
		$.ajax({
			url:url,
			type:"GET",
			dataType:"json",
			data: params,
			success:function(data) {
				me.set('content',[]);
				
				
				$.each(data.entries, function(i, item) {
					var video = App.Video.create({
						author:			item.Author,
						id:				item.VideoID,
						title:			item.Title,
						category: 		item.Category,
						description:	item.Description,
						keywords:		item.Keywords,
						thumbnail:		item.Thumbnails[0].url,
						duration:		item.Duration,
						published:		item.Published,
						viewCount:		item.Views,
						likesCount:		item.Likes
						
					});
					
					me.pushObject(video);
				});
			}
		});
	},
	goToAddVideo: function() {
		window.location = "/application/addvideo";
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
	selected: null,
	getCategories: function(selectedCategory) {
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
				
				App.categoryController.set('selected', selectedCategory);
			}
		});
		
		
	}
});

App.roleController = Ember.ArrayController.create({
	
	selected: null,	
	content: [
	App.Role.create({
  		term: 'false',
  		label: 'Curator'
  }),
  App.Role.create({
  		term: 'false',
  		label: 'Administrator'
  })]
});

function onYouTubeIframeAPIReady() {
		App.authenticationController.openYoutubeAPI("playlist");
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
