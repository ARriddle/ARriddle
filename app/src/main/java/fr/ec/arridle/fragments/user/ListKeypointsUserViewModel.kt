package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import android.util.Log
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

    // LiveData to handle navigation to the selected property
    private val _navigateToSelectedKeypoint = MutableLiveData<KeypointProperty>()
    val navigateToSelectedKeypoint: LiveData<KeypointProperty>
        get() = _navigateToSelectedKeypoint

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
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param keypointProperty The [keypointProperty] that was clicked on.
     */
    fun displayKeypointDetails(keypointProperty: KeypointProperty) {
        _navigateToSelectedKeypoint.value = keypointProperty
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayKeypointDetailsComplete() {
        _navigateToSelectedKeypoint.value = null
    }
}
