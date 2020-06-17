package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.ec.arridle.findRankById
import fr.ec.arridle.findUserById
import fr.ec.arridle.network.API
import fr.ec.arridle.network.KeypointProperty
import fr.ec.arridle.network.UserProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class GameViewModel(application: Application) : AndroidViewModel(application) {
    // The internal MutableLiveData String that stores the most recent response
    private val _property = MutableLiveData<UserProperty>()
    private val _rank = MutableLiveData<Int>()
    private val _keypoints = MutableLiveData<List<KeypointProperty>>()
    private val _navigateToSelectedKeypoint = MutableLiveData<KeypointProperty>()
    // The external immutable LiveData for the response String

    val property: LiveData<UserProperty>
        get() = _property

    val rank: LiveData<Int>
        get() = _rank

    val keypoints: LiveData<List<KeypointProperty>>
        get() = _keypoints

    val navigateToSelectedKeypoint: LiveData<KeypointProperty>
        get() = _navigateToSelectedKeypoint

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {
        getUsersProperties()
        getKeypointsProperties()
    }

    private fun getUsersProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref.getString("game_id", null)
            val userId = sharedPref.getInt("user_id", -1)
            val users = API.retrofitService.getUsersAsync(game_id = gameId!!)
            try {
                val listResult = users.await()
                val user = findUserById(listResult, userId)
                val place = findRankById(listResult, user)
                _property.value = user
                _rank.value = place
            } catch (e: Exception) {
                _rank.value = null
                _property.value = null
                e.printStackTrace()
            }
        }
    }

    fun getKeypointsProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences("connection",Context.MODE_PRIVATE)
            val gameId = sharedPref.getString("game_id", null)
            val getKeypointsDeferred = API.retrofitService.getKeypointsAsync(game_id = gameId!!)

            try {
                val listResult = getKeypointsDeferred.await()
                _keypoints.value = listResult
            } catch (e: Exception) {
                _keypoints.value = ArrayList()
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