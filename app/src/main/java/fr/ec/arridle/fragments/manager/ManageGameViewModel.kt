package fr.ec.arridle.fragments.manager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import fr.ec.arridle.network.API
import kotlinx.coroutines.*

class ManageGameViewModel(application: Application) : AndroidViewModel(application) {
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
    suspend fun deleteGameProperties(gameId: String?) {
        coroutineScope {
            launch {
                val post =
                    API.retrofitService.deleteGameAsync(game_id = gameId!!)
                try {
                    post.await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}