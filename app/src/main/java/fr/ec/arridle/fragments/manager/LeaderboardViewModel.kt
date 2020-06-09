package fr.ec.arridle.fragments.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.ec.arridle.network.GameAPI
import fr.ec.arridle.network.GameProperty
import fr.ec.arridle.network.UserAPI
import fr.ec.arridle.network.UserProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LeaderboardViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _properties = MutableLiveData<List<UserProperty>>()

    // The external immutable LiveData for the response String

    val properties: LiveData<List<UserProperty>>
        get() = _properties

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {
        getUserProperties()
    }

    private fun getUserProperties() {
        coroutineScope.launch {
            var getPropertiesDeferred = UserAPI.retrofitService.getProperties()
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