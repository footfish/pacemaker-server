package controllers

import java.util.UUID;
import models.Activity
import models.Location
import models.User
import models.Message

class PacemakerAPI {
  val adminUser = User(firstname="power", lastname="user",email="admin@localhost",password="admin",admin=true) 
  var userIndex = hashMapOf<String, User>(adminUser.id to adminUser)
  var emailIndex = hashMapOf<String, User>(adminUser.email to adminUser)
  var activitiesIndex = hashMapOf<String, Activity>()
  var users = userIndex.values
    
  fun createUser(firstName: String, lastName: String, email: String, password: String): User {
    var user = User(firstName, lastName, email, password)
    userIndex[user.id] = user
    emailIndex[user.email] = user
    return user
  }
  
  fun deleteUsers() {
    userIndex.clear();
    emailIndex.clear()
  }
  
  fun getUser(id: String) = userIndex[id]
  fun getUserByEmail(email: String) = emailIndex[email]
  
  fun createActivity(id: String, type: String, location: String, distance: Float): Activity? {
    var activity:Activity? = null
    var user = userIndex.get(id)
    if (user != null) {
      activity = Activity(type, location, distance)
      user.activities[activity.id] = activity
      activitiesIndex[activity.id] = activity;
    }
    return activity;
  }
  
	  fun createLocation(activityId: String, location: Location): Activity? {
		  activitiesIndex[activityId]?.route?.add(location)
    return activitiesIndex[activityId]
  }

	fun getActivity(id: String): Activity? {
    return activitiesIndex[id]
  }
	
	fun getFriends(id:String): MutableCollection<User?>? {
	val friendIndex = hashMapOf<String, User?>()
		if(userIndex[id] != null) {
		  for (i in userIndex[id]?.friend!!) {
		    friendIndex[i] = userIndex[i]
		  }
			return friendIndex.values
		} else {
			  return null
		}
	}
	
  fun createFriend(id: String, email: String): Boolean{
			val friendUser = emailIndex[email]
			val user = userIndex[id]
			if (user != null && friendUser != null ) {
				if (friendUser != user) { // can't add yourself as friend
				  user.friend.add(friendUser.id)     //add friend 
				  friendUser.friend.add(user.id)     //then add mutual relationship
					return true
				}  
			}
	  return false
  }	
 	
  fun deleteFriend(id: String, email: String) {
			val friendUser = emailIndex[email]
			val user = userIndex[id]
			if (user != null && friendUser != null ) {
			  user.friend.remove(friendUser.id)     //remove friend 
			  friendUser.friend.remove(user.id)     //then remove mutual relationship  
			}	
  }
	
	  fun getFriendActivities(id: String, email: String): MutableCollection<Activity>? {
			val friendUser = emailIndex[email]
			val user = userIndex[id]
			if (user != null && friendUser != null ) {
			  if (user.friend.contains(friendUser.id) ) { // check they are friends 
				  return friendUser.activities.values
				}  
			}
		  return null
	  }	
  
	
  fun deleteActivities(id: String) {
    require(userIndex[id] != null)
    var user = userIndex.get(id)
    if (user != null) {
      for ( activity in user.activities.values) {		
        activitiesIndex.remove(activity.id)
      }
      user.activities.clear();
    }
  }
	
	fun getMessages(id: String):MutableList<Message> {
    var user = userIndex.get(id)
	  val prettyMessages:MutableList<Message> = ArrayList() //Messages stores id in 'from' so we got to rebuild with email address.
		if (user != null)	{ 
		  for (message in user.messages.values) {
				  prettyMessages.add(Message(message=message.message, from=userIndex.get(message.from)!!.email, id=message.id))
		  }
		}
	 return prettyMessages	
	}
	
  fun sendMessage(id: String, email: String, message: Message) {
			val friendUser = emailIndex[email]
			val user = userIndex[id]
 			if (user != null && friendUser != null ) {
			  if (user.friend.contains(friendUser.id) ) { // check they are friends 
				    friendUser.messages[message.id]=message
				}  
			}
  }
	
  fun broadcastMessage(id: String, message: Message) {
			val user = userIndex[id]
 			if (user != null) {
				  for (i in userIndex[id]?.friend!!) {
					   val friendUser = userIndex[i]
					   if (friendUser != null) { //in case user was deleted 
					   friendUser.messages[message.id]=message
					   }
				  }
			}	
  }	  	
		  	
}