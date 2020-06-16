package fr.ec.arridle.fragments.manager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.ec.arridle.network.API
import kotlinx.coroutines.*

class CreateGameViewModel(application: Application) : AndroidViewModel(application) {
    var gameId = MutableLiveData<String>()

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

    suspend fun postGameProperties(
        name: String,
        duration: Int?,
        timeStart: Int?,
        nbPlayerMax: Int,
        visibility: Boolean
    ) {
        coroutineScope {
            launch {
                val post = API.retrofitService.postGameAsync(
                    name = name,
                    duration = duration!!,
                    timeStart = timeStart,
                    visibility = visibility,
                    nbPlayerMax = nbPlayerMax
                )
                try {
                    val game = post.await()
                    gameId.value = game.id
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}