package models

import java.util.UUID

data class Activity(
    var type: String = "",
    var location: String = "",
    var distance: Float = 0.0f,
    val id: String = UUID.randomUUID().toString(),
    var route: MutableList<Location> = ArrayList())
