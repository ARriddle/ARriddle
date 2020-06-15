package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import fr.ec.arridle.findUserById
import fr.ec.arridle.network.*
import kotlinx.coroutines.*

class KeypointViewModel(keypointId: Int, app: Application) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected property
    private val _keypointProperty = MutableLiveData<KeypointProperty>()

    private val _user = MutableLiveData<UserProperty>()

    // The external LiveData for the SelectedProperty
    val keypointProperty: LiveData<KeypointProperty>
        get() = _keypointProperty

    val user: LiveData<UserProperty>
        get() = _user

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )


    // Initialize the _selectedProperty MutableLiveData
    init {
        getKeypointProperty(keypointId)
        getUserProperties()
    }

    private fun getKeypointProperty(keypointId: Int) {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref.getString("game_id", null)
            val keypoint =
                API.retrofitService.getKeypointAsync(game_id = gameId!!, keypoint_id = keypointId)

            try {
                val getKeypoint = keypoint.await()
                _keypointProperty.value = getKeypoint
            } catch (e: Exception) {
                _keypointProperty.value = null
                e.printStackTrace()
            }
        }
    }

    private fun getUserProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref.getString("game_id", null)
            val userId = sharedPref.getInt("user_id", -1)
            val user = API.retrofitService.getUserAsync(game_id = gameId!!, user_id = userId)

            try {
                val user = user.await()
                _user.value = user

            } catch (e: Exception) {
                _user.value = null
                e.printStackTrace()
            }
        }
    }
    suspend fun putUserProperties(gameId: String?, userId: Int?, points: Int){
        coroutineScope {
            launch {
                val put =
                    API.retrofitService.putUserAsync(game_id = gameId!!, user_id = userId!!, user = PutUserProperty(points=points))
                try {
                    val p = put.await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}