package models

data class Location(
    var latitude: Double = 0.0,     //added default values here, otherwise json deserialisation failed. 
    var longitude: Double = 0.0)

