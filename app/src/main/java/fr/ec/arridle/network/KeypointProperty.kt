package fr.ec.arridle.network

import com.squareup.moshi.Json

data class KeypointProperty (
    val id: Int,
    val name: String,
    val points: Int,
    @Json(name = "url_cible") val urlCible: String,
    val latitude: Double,
    val longitude: Double,
    @Json(name = "users_solvers") val usersSolvers: List<UserProperty>,
    @Json(name = "game_id") val gameId: String
)