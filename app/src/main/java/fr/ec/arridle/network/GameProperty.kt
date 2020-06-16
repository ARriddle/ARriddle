package fr.ec.arridle.network

import com.squareup.moshi.Json

data class GameProperty(
    val id: String,
    val name: String,
    val visibility: Boolean,
    val duration: Int,
    @Json(name = "time_start") val timeStart: Int,
    @Json(name = "nb_player_max") val nbPlayerMax: Int,
    val keypoints: List<KeypointProperty> = emptyList(),
    val users: List<UserProperty> = emptyList()
)