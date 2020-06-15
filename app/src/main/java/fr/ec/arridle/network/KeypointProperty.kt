package fr.ec.arridle.network

import com.squareup.moshi.Json

data class KeypointProperty (
    val id: Int,
    val name: String,
    val description: String,
    val solution: String,
    val points: Int,
    @Json(name = "url_cible") val urlCible: String,
    val latitude: Double,
    val longitude: Double,
    @Json(name = "game_id") val gameId: String
)