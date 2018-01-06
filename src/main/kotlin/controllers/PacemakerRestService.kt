package controllers

import io.javalin.Context
import models.Activity
import models.Location
import models.User
import models.Message
import java.net.URLDecoder

class PacemakerRestService  {
  val pacemaker = PacemakerAPI()

  fun getUsers(ctx: Context) {
		val id: String? = ctx.queryParam("id") //optional 'id' filter
		var email: String? = ctx.queryParam("email") //optional 'email' filter
	  if (email != null) {
		  email = URLDecoder.decode(email, "UTF-8")
			}
	  	  
	  if (id == null && email == null) {
	    ctx.json(pacemaker.users)		  
			} else if (id != null && email == null) {
			  ctx.json(pacemaker.users.filter { it.id == id })
	  	} else if (id == null && email != null) {
	   	  ctx.json(pacemaker.users.filter { it.email == email })
	  	} else if (id != null && email != null) {
	   	  ctx.json(pacemaker.users.filter { it.email == email && it.id == id })
	    }
    }
  
  fun createUser(ctx: Context) {
    val user = ctx.bodyAsClass(User::class.java)
    val newUser = pacemaker.createUser(user.firstname, user.lastname, user.email, user.password)
	  if (newUser != null) {
	    ctx.json(newUser)
	  } else {
      ctx.status(418) //418 I'm a teapot
			}
  }
	
  fun updateUser(ctx: Context) {
    val id: String? =  ctx.param("id")
		if (id != null) {		  
		  val user = ctx.bodyAsClass(User::class.java)
		      val updatedUser = pacemaker.updateUser(id, user.firstname, user.lastname, user.email, user.password, user.disabled, user.admin)
		        ctx.json(updatedUser)
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }

  fun deleteUsers(ctx: Context) {
    pacemaker.deleteUsers()
    ctx.status(204)  //204 No Content
  }
    
	fun deleteUser(ctx: Context) {
    val id: String? =  ctx.param("id")
		if (id != null) {		  
		  if (pacemaker.deleteUser(id)){
           ctx.status(204) //204 No Content
         } else {
           ctx.status(404)  //404 Not Found
				 }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
	}
	
  fun getActivity(ctx: Context) {
    // val userId: String? = ctx.param("id")  //should verify user is correct here 
    val activityId: String? = ctx.param("activityId")
    val activity = pacemaker.getActivity(activityId!!)
    if (activity != null) {
      ctx.json(activity)
    } else {
      ctx.status(404)  //404 Not Found
    }
  }

	  fun getActivities(ctx: Context) {
    val id: String? =  ctx.param("id")
		if (id != null) {		  
		  val user = pacemaker.getUser(id)
		  if (user != null) {
		    val type: String? = ctx.queryParam("type") //optional 'type' filter
		    if (type != null) {
		      ctx.json(user.activities.values.filter { it.type == type })
		    } else {
		      ctx.json(user.activities.values)
		    }
		  } else {
		    ctx.status(404)  //404 Not Found
		  }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }

    fun createActivity(ctx: Context) {
    val id: String? =  ctx.param("id")
		if (id != null) {
		  val user = pacemaker.getUser(id)
		      if (user != null) {
		        val activity = ctx.bodyAsClass(Activity::class.java)
		        val newActivity = pacemaker.createActivity(user.id, activity.type, activity.location, activity.distance)
		        ctx.json(newActivity!!)
		      } else {
		        ctx.status(404)  //404 Not Found
		      }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }
	
   fun createFriend(ctx: Context) {
     val id: String? =  ctx.param("id")
     val friendEmail = URLDecoder.decode(ctx.param("email"),"UTF-8")
       if (id != null && friendEmail != null) {
         if (pacemaker.createFriend(id, friendEmail)) {
           ctx.status(204) //204 No Content
         } else {
           ctx.status(404)  //404 Not Found
         }
       } else {
         ctx.status(422) //422 Unprocessable Entity
       }
   }
		
   fun deleteFriend(ctx: Context) {
     val id: String? =  ctx.param("id")
     val friendEmail = URLDecoder.decode(ctx.param("email"),"UTF-8")
     if (id != null && friendEmail != null) {
       if(pacemaker.deleteFriend(id, friendEmail)) {
         ctx.status(204) //204 No Content
       } else {
         ctx.status(404)  //404 Not Found
       }
     } else {
       ctx.status(422) //422 Unprocessable Entity
     }
   }
  
	 fun getFriends(ctx: Context) {
	   val id: String? =  ctx.param("id")
	   if (id != null) {
	     val friendsIndex = pacemaker.getFriends(id)
	     if (friendsIndex != null) {
	       ctx.json(friendsIndex)
	     } else {
	       ctx.status(404)  //404 Not Found
	     }
	   } else {
	     ctx.status(422) //422 Unprocessable Entity
	   }
	 }
	
  fun getFriendActivities(ctx: Context) {
    val id: String? =  ctx.param("id")
    val friendEmail = URLDecoder.decode(ctx.param("email"),"UTF-8")
    if (id != null && friendEmail != null) {
		  val activities = pacemaker.getFriendActivities(id, friendEmail)
		  if ( activities != null ) {
		    ctx.json(activities)
		  } else {
		    ctx.status(404) //404 Not Found
		  }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }	
  
  fun deleteActivites(ctx: Context) {
    val id: String? =  ctx.param("id")
	  if (id != null) {
	    if(pacemaker.deleteActivities(id)) {
	      ctx.status(204) //204 No Content
	    } else {
	      ctx.status(404) //404 Not Found
		  }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }
	
  fun createLocation(ctx: Context) {
		val location = ctx.bodyAsClass(Location::class.java)
    val activityId: String? = ctx.param("activityId")
	  if (activityId !=null ){
	    val activity = pacemaker.getActivity(activityId)
	    if (activity != null) {
	      val newActivity = pacemaker.createLocation(activity.id, location)
	      if (newActivity != null) {
	        ctx.json(newActivity)
	      } else {
	        ctx.status(404)  //404 Not Found
	      }
	    } else {
	      ctx.status(404)   //404 Not Found
	    }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }
	
fun sendMessage(ctx: Context) {
    val id: String? =  ctx.param("id")
    val friendEmail = URLDecoder.decode(ctx.param("email"),"UTF-8")
	  val message = ctx.bodyAsClass(Message::class.java)
    if (id != null && friendEmail != null) {
	    if( pacemaker.sendMessage(id, friendEmail, message)) {
	      ctx.status(204) //204 No Content			
	    } else {
	      ctx.status(404)  //404 Not Found
	    }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }	

fun getMessages(ctx: Context) {
    val id: String? =  ctx.param("id")
	  if (id != null) {
	    ctx.json(pacemaker.getMessages(id))
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
   }
	
fun broadcastMessage(ctx: Context) {
    val id: String? =  ctx.param("id")
	  val message = ctx.bodyAsClass(Message::class.java)
    if (id != null) {
	    if (pacemaker.broadcastMessage(id, message)) {
	      ctx.status(204) //204 No Content
	    } else {
	      ctx.status(404)  //404 Not Found
	    }
		} else {
      ctx.status(422) //422 Unprocessable Entity
    }
  }	
	
}