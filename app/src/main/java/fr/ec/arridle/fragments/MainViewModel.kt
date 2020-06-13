package fr.ec.arridle.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.network.API
import fr.ec.arridle.network.GameProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _properties = MutableLiveData<List<GameProperty>>()

    // The external immutable LiveData for the response String

    val properties: LiveData<List<GameProperty>>
        get() = _properties

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {
        getGameProperties()
    }

    private fun getGameProperties() {
        coroutineScope.launch {
            val getPropertiesDeferred = API.retrofitService.getGamesAsync()

            try {
                val listResult = getPropertiesDeferred.await()
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
