package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import fr.ec.arridle.network.*
import kotlinx.coroutines.*

class KeypointViewModel(keypointId: Int, app: Application) : AndroidViewModel(app) {

    // The internal MutableLiveData for the selected property
    private val _keypointProperty = MutableLiveData<KeypointProperty>()

    private val _user = MutableLiveData<UserProperty>()
    private val _solves = MutableLiveData<List<SolveProperty>>()


    // The external LiveData for the SelectedProperty
    val keypointProperty: LiveData<KeypointProperty>
        get() = _keypointProperty

    val user: LiveData<UserProperty>
        get() = _user

    val solves: LiveData<List<SolveProperty>>
        get() = _solves

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )


    // Initialize the _selectedProperty MutableLiveData
    init {
        getKeypointProperty(keypointId)
        getUserProperties()
        getSolvesProperties()
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
                val result = user.await()
                _user.value = result

            } catch (e: Exception) {
                _user.value = null
                e.printStackTrace()
            }
        }
    }

    private fun getSolvesProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref.getString("game_id", null)
            val solves = API.retrofitService.getSolvesAsync(game_id = gameId!!)

            try {
                val listResult = solves.await()
                _solves.value = listResult

            } catch (e: Exception) {
                _solves.value = null
                e.printStackTrace()
            }
        }
    }

    suspend fun putUserProperties(gameId: String?, userId: Int?, points: Int) {
        coroutineScope {
            launch {
                val put =
                    API.retrofitService.putUserAsync(
                        game_id = gameId!!,
                        user_id = userId!!,
                        user = PutUserProperty(points = points)
                    )
                try {
                    val p = put.await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    suspend fun postSolve(gameId: String?, userId: Int?, keypointId: Int?) {
        coroutineScope {
            launch {
                val put =
                    API.retrofitService.postSolveAsync(
                        game_id = gameId!!,
                        user_id = userId!!,
                        keypoint_id = keypointId!!
                    )
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