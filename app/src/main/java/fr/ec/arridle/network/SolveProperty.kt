package fr.ec.arridle.network


import com.squareup.moshi.Json

data class SolveProperty (
    @Json(name = "user_id")val userId: Int,
    @Json(name = "keypoint_id")val keypointId: Int,
    @Json(name = "game_id")val gameId: String
)