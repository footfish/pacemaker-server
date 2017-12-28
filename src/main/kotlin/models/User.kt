package models

import java.util.UUID

data class User(
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var password: String = "",
    val id: String = UUID.randomUUID().toString(),
	  var friend: MutableSet<String> = mutableSetOf(),
    val activities: MutableMap<String, Activity> = hashMapOf<String, Activity>())
