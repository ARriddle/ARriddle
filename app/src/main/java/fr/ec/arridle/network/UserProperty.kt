package fr.ec.arridle.network

import com.squareup.moshi.Json

data class UserProperty(
    val id: Int,
    val name: String,
    val points: Int,
    @Json(name = "game_id") val gameId: String,
    @Json(name = "keypoints_solved") val keypointsSolved: List<KeypointProperty> = emptyList()
)

data class PutUserProperty(val name: String)

data class PutPointsUserProperty(val points: Int)