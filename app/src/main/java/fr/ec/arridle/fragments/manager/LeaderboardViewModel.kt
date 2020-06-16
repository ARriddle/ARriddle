package fr.ec.arridle.fragments.manager

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.ec.arridle.network.API
import fr.ec.arridle.network.GameProperty
import fr.ec.arridle.network.UserProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LeaderboardViewModel(application: Application) : AndroidViewModel(application) {
    // The internal MutableLiveData String that stores the most recent response
    private val _properties = MutableLiveData<List<UserProperty>>()
    private val _game = MutableLiveData<String>()


    // The external immutable LiveData for the response String

    val properties: LiveData<List<UserProperty>>
        get() = _properties

    val game: LiveData<String>
        get() = _game

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {
        getUserProperties()
        getGameProperties()
    }

    private fun getUserProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref.getString("game_id", null)
            val getPropertiesDeferred = API.retrofitService.getUsersAsync(game_id = gameId!!)
            try {
                val listResult = getPropertiesDeferred.await()
                val listSorted = listResult.sortedByDescending { it.points }
                _properties.value = listSorted
            } catch (e: Exception) {
                _properties.value = ArrayList()
                e.printStackTrace()
            }
        }
    }

    private fun getGameProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref.getString("game_id", null)
            val getPropertiesDeferred = API.retrofitService.getGameAsync(game_id = gameId!!)
            try {
                val game = getPropertiesDeferred.await()
                _game.value = game.name
            } catch (e: Exception) {
                _game.value = null
                e.printStackTrace()
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}