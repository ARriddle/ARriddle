package fr.ec.arridle.fragments.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.ec.arridle.network.GameAPI
import fr.ec.arridle.network.KeypointProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListKeypointsViewModel : ViewModel() {
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
            var getKeypointsDeferred = GameAPI.retrofitService.getKeypoints()

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
