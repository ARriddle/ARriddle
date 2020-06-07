package fr.ec.arridle.network

import com.squareup.moshi.Json

data class KeypointProperty (
    val id: String,
    val name: String,
    @Json(name = "points") val nbPoints: Double,
    @Json(name = "url_cible") val urlCible: String,
    @Json(name = "url_audio") val urlAudio: String,
    @Json(name = "game_id") val gameId: String

)