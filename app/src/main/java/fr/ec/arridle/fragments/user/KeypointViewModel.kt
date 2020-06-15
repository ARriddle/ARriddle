package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import fr.ec.arridle.network.API
import fr.ec.arridle.network.KeypointProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class KeypointViewModel( keypointId: Int,
                       app: Application
) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected property
    private val _keypointProperty = MutableLiveData<KeypointProperty>()

    // The external LiveData for the SelectedProperty
    val keypointProperty: LiveData<KeypointProperty>
        get() = _keypointProperty

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )


    // Initialize the _selectedProperty MutableLiveData
    init {
        getKeypointProperty(keypointId)
    }
    private fun getKeypointProperty(keypointId: Int) {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences("connection",
                Context.MODE_PRIVATE)
            val gameId = sharedPref.getString("game_id", null)
            val keypoint = API.retrofitService.getKeypointAsync(game_id = gameId!!, keypoint_id = keypointId)

            try {
                val getKeypoint = keypoint.await()
                _keypointProperty.value = getKeypoint
            } catch (e: Exception) {
                _keypointProperty.value = null
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}