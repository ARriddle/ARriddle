package fr.ec.arridle.network

import com.squareup.moshi.Json

data class UserProperty (
    val id: String,
    val name: String,
    val duration: Double,
    @Json(name = "time_start") val timeStart: Double,
    @Json(name = "nb_player") val nbPlayer: Double,
    @Json(name = "nb_player_max") val nbPlayerMax: Double,
    val keypoints: List<KeypointProperty>
)