package fr.ec.arridle.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
    fun getKeypointsAsync(
        @Path(value = "game_id", encoded = true) game_id: String,
        @Path(value = "keypoint_id", encoded = true) keypoint_id: String
    ): Deferred<List<KeypointProperty>>

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
        @Path(value = "user_id", encoded = true) user_id: String
    ): Deferred<List<UserProperty>>

    // -------------------- POST ----------------------

    @POST("games/{game_id}/users")
    fun postUserAsync(
        @Path(value = "game_id", encoded = true) game_id: String, @Body user: PostUserProperty
    ): Deferred<UserProperty>

} 


object API {
    val retrofitService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}
