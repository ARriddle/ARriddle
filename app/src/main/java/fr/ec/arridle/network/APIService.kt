package fr.ec.arridle.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = " https://arriddle.rezoleo.fr"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface APIService {

    // -------------------- GET ----------------------

    @GET("games")
    fun getGamesAsync(): Deferred<List<GameProperty>>

    @GET("games/{game_id}")
    fun getGameAsync(
        @Path(
            value = "game_id",
            encoded = true
        ) game_id: String
    ): Deferred<GameProperty>

    @GET("games/{game_id}/keypoints")
    fun getKeypointsAsync(
        @Path(
            value = "game_id",
            encoded = true
        ) game_id: String
    ): Deferred<List<KeypointProperty>>

    @GET("games/{game_id}/keypoints/{keypoint_id}")
    fun getKeypointAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Path(value = "keypoint_id", encoded = true) keypoint_id: Int
    ): Deferred<KeypointProperty>

    @GET("games/{game_id}/users")
    fun getUsersAsync(
        @Path(
            value = "game_id",
            encoded = true
        ) game_id: String
    ): Deferred<List<UserProperty>>

    @GET("games/{game_id}/users/{user_id}")
    fun getUserAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Path(value = "user_id", encoded = true) user_id: Int
    ): Deferred<UserProperty>

    @GET("games/{game_id}/solves")
    fun getSolvesAsync(
        @Path(value = "game_id", encoded = true) game_id: String
    ): Deferred<List<SolveProperty>>


    // -------------------- POST ----------------------

    @POST("games/{game_id}/users")
    fun postUserAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Query("name") name: String,
        @Query("points") points: Int = 0
    ): Deferred<UserProperty>

    @POST("games/{game_id}/keypoints")
    fun postKeypointAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Query("name") name: String,
        @Query("description") description: String,
        @Query("solution") solution: String,
        @Query("points") points: Int = 0,
        @Query("url_cible") urlCible: String? = null,
        @Query("latitude") latitude : Double? = null ,
        @Query("longitude") longitude : Double? = null
    ): Deferred<KeypointProperty>

    @POST("games")
    fun postGameAsync(
        @Query("name") name: String,
        @Query("visibility") visibility: Boolean,
        @Query("duration") duration: Int,
        @Query("time_start") timeStart: Int? = null,
        @Query("nb_player_max") nbPlayerMax: Int? = null
        ): Deferred<GameProperty>

    @POST("games/{game_id}/solves")
    fun postSolveAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Query("user_id") user_id: Int,
        @Query("keypoint_id") keypoint_id: Int
    ): Deferred<SolveProperty>

    // -------------------- DELETE ---------------------
    @DELETE("games/{game_id}/users/{user_id}")
    fun deleteUserAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Path(value = "user_id", encoded = true) user_id: Int
    ): Deferred<Unit>

    @DELETE("games/{game_id}/keypoints/{keypoint_id}")
    fun deleteKeypointAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Path(value = "keypoint_id", encoded = true) keypoint_id: Int
    ): Deferred<Unit>

    @DELETE("games/{game_id}")
    fun deleteGameAsync(
        @Path(value = "game_id", encoded = true) game_id: String
    ): Deferred<Unit>

    // --------------------- PUT ---------------------
    @PUT("games/{game_id}/users/{user_id}")
    fun putUserAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Path(value = "user_id", encoded = true) user_id: Int,
        @Body user: PutUserProperty
    ): Deferred<UserProperty>


}


object API {
    val retrofitService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}
