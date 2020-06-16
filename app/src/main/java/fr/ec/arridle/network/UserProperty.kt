package fr.ec.arridle.network

import com.squareup.moshi.Json

data class UserProperty(
    val id: Int,
    val name: String,
    val points: Int,
    @Json(name = "game_id") val gameId: String
)

data class PutUserProperty(
    val name: String? = null,
    val points: Int? = null
)

