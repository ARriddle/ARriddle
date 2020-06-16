package fr.ec.arridle.fragments.manager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.ec.arridle.network.API
import kotlinx.coroutines.*

class CreateKeypointViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    suspend fun postKeypointProperties(
        name: String,
        description: String,
        solution: String,
        points: Int?,
        gameId: String
    ) {
        coroutineScope {
            launch {
                val post = API.retrofitService.postKeypointAsync(
                    name = name,
                    description = description,
                    solution = solution,
                    points = points ?: 0,
                    game_id = gameId
                )
                try {
                    val game = post.await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}