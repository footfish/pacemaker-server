package controllers

import io.javalin.Context
import models.Activity
import models.Location
import models.User

class PacemakerRestService  {
  val pacemaker = PacemakerAPI()

  fun listUsers(ctx: Context) {
    ctx.json(pacemaker.users)
  }
  
  fun createUser(ctx: Context) {
    val user = ctx.bodyAsClass(User::class.java)
    val newUser = pacemaker.createUser(user.firstname, user.lastname, user.email, user.password)
    ctx.json(newUser)
  }

  fun deleteUsers(ctx: Context) {
    pacemaker.deleteUsers()
    ctx.status(204)
  }
    
  fun getActivity(ctx: Context) {
    // val userId: String? = ctx.param("id")
    val activityId: String? = ctx.param("activityId") 
    val activity = pacemaker.getActivity(activityId!!)
    if (activity != null) {
      ctx.json(activity)
    } else {
      ctx.status(404)
    }
  }

	
	
  fun getActivities(ctx: Context) {
    val id: String? =  ctx.param("id")
    val user = pacemaker.getUser(id!!)
    if (user != null) {
      ctx.json(user.activities.values)
    } else {
      ctx.status(404)
    }
  }

  fun getActivitiesType(ctx: Context) {
    val id: String? =  ctx.param("id")
	  val type: String? =  ctx.param("type")
    val user = pacemaker.getUser(id!!)
    if (user != null) {
      ctx.json(user.activities.values.filter { it.type == type })
    } else {
      ctx.status(404)
    }
  }
	
	
   fun createActivity(ctx: Context) {
    val id: String? =  ctx.param("id")
    val user = pacemaker.getUser(id!!)
    if (user != null) {
      val activity = ctx.bodyAsClass(Activity::class.java)
      val newActivity = pacemaker.createActivity(user.id, activity.type, activity.location, activity.distance)
      ctx.json(newActivity!!)
    } else {
      ctx.status(404)
    }
  }
  
  fun deleteActivites(ctx: Context) {
    val id: String? =  ctx.param("id")
    pacemaker.deleteActivities(id!!);
    ctx.status(204)
  }
	
	 fun createLocation(ctx: Context) {
		println("creating location")
		val location = ctx.bodyAsClass(Location::class.java)
		println(location)
    val activityId: String? = ctx.param("activityId")
		println(activityId)
    val activity = pacemaker.getActivity(activityId!!)
		println(activity)
    if (activity != null) {
			println("attempt adding location pacemaker.createLocation()")
      val newActivity = pacemaker.createLocation(activity.id, location)
      ctx.json(newActivity!!)
    } else {
      ctx.status(404)
    }
  }
	
}