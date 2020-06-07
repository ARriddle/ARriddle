package fr.ec.arridle.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.ec.arridle.network.GameAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main)

    init {
        getGameProperties()
    }
    private fun getGameProperties() {
        coroutineScope.launch {
            var getPropertiesDeferred = GameAPI.retrofitService.getProperties()
            try {
                var listResult = getPropertiesDeferred.await()
                _response.value =
                    "Success: ${listResult.size} Games properties retrieved"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
