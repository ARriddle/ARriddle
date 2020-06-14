package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.network.API
import fr.ec.arridle.network.KeypointProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListKeypointsUserViewModel(application: Application) : AndroidViewModel(application) {
    // The internal MutableLiveData String that stores the most recent response
    private val _properties = MutableLiveData<List<KeypointProperty>>()

    // The external immutable LiveData for the response String

    val properties: LiveData<List<KeypointProperty>>
        get() = _properties

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {
        getKeypointsProperties()
    }

    private fun getKeypointsProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences("connection",Context.MODE_PRIVATE)
            val gameId = sharedPref.getString("game_id", null)
            val getKeypointsDeferred = API.retrofitService.getKeypointsAsync(game_id = gameId!!)

            try {
                val listResult = getKeypointsDeferred.await()
                _properties.value = listResult
            } catch (e: Exception) {
                _properties.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
