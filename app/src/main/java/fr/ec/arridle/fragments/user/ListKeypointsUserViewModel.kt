package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.network.API
import fr.ec.arridle.network.KeypointProperty
import fr.ec.arridle.network.SolveProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListKeypointsUserViewModel(application: Application) : AndroidViewModel(application) {
    // The internal MutableLiveData String that stores the most recent response
    private val _solves = MutableLiveData<List<SolveProperty>>()

    // The external immutable LiveData for the response String

    private val _properties = MutableLiveData<List<KeypointProperty>>()
    val properties: LiveData<List<KeypointProperty>>
        get() = _properties

    // LiveData to handle navigation to the selected property
    private val _navigateToSelectedKeypoint = MutableLiveData<KeypointProperty>()
    val navigateToSelectedKeypoint: LiveData<KeypointProperty>
        get() = _navigateToSelectedKeypoint
    val solves: LiveData<List<SolveProperty>>
        get() = _solves
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {
        getKeypointsProperties()
    }

    suspend fun getKeypointsPropertiesSync(): List<KeypointProperty> {
        val sharedPref = getApplication<Application>().getSharedPreferences(
            "connection",
            Context.MODE_PRIVATE
        )
        val gameId = sharedPref.getString("game_id", null)
        val getKeypointsDeferred = API.retrofitService.getKeypointsAsync(game_id = gameId!!)

        kotlin.runCatching {
            getKeypointsDeferred.await()
        }.onSuccess {
            return it
        }

        return emptyList()
    }

    fun getKeypointsProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref.getString("game_id", null)
            val userId = sharedPref?.getInt("user_id", -1)
            val getKeypointsDeferred = API.retrofitService.getKeypointsAsync(game_id = gameId!!)
            val solves = API.retrofitService.getSolvesAsync(game_id = gameId!!)

            try {
                val listResult1 = getKeypointsDeferred.await()
                val listResult2 = solves.await()
                val solvesFiltered = listResult2.filter { it.userId == userId && it.gameId == gameId }
                listResult1.forEach {
                    it.isValidate =
                        solvesFiltered.any { keypoint -> it.id == keypoint.keypointId }
                }
                _properties.value = listResult1
                _solves.value = listResult2
                Log.i("azer", _properties.value.toString())
                Log.i("azer", _solves.value.toString())

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
