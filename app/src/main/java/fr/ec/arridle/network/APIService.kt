package fr.ec.arridle.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

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
    @GET("games")
    fun getGamesAsync(): Deferred<List<GameProperty>>

    @GET("keypoints")
    fun getKeypointsAsync(): Deferred<List<KeypointProperty>>

    @GET("users")
    fun getUsersAsync(): Deferred<List<UserProperty>>
}

object API {
    val retrofitService : APIService by lazy {
        retrofit.create(APIService::class.java) }
}
