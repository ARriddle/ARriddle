package fr.ec.arridle.network

import com.squareup.moshi.Json

data class UserProperty(
    val id: Int,
    val name: String,
    val points: Int,
    val game_id: String,
    @Json(name = "keypoints_solved") val keypointsSolved: List<KeypointProperty>
)

data class PostUserProperty(
    val name: String,
    val points: Int,
    val game_id: String
)