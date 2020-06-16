package fr.ec.arridle.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.ec.arridle.network.API
import fr.ec.arridle.network.GameProperty
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    // The internal MutableLiveData String that stores the most recent response
    private val _properties = MutableLiveData<List<GameProperty>>()

    // The external immutable LiveData for the response String
    private val _navigateToSelectedGame = MutableLiveData<GameProperty>()
    var userId = MutableLiveData<Int>()

    val properties: LiveData<List<GameProperty>>
        get() = _properties

    val navigateToSelectedGame: LiveData<GameProperty>
        get() = _navigateToSelectedGame



    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )


    init {
        getGameProperties()
    }

    fun getGameProperties() {
        coroutineScope.launch {
            val getPropertiesDeferred = API.retrofitService.getGamesAsync()

            try {
                val listResult = getPropertiesDeferred.await()
                val list = listResult.filter { it.visibility }
                _properties.value = list
            } catch (e: Exception) {
                _properties.value = ArrayList()
                e.printStackTrace()

            }
        }
    }

    suspend fun postUserProperties(id: String, pseudo: String) {
        coroutineScope {
            launch {
                val post = API.retrofitService.postUserAsync(id, pseudo)
                try {
                    val user = post.await()
                    userId.value = user.id
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

    /**
     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param keypointProperty The [keypointProperty] that was clicked on.
     */
    fun displayGameDetails(gameProperty: GameProperty) {
        _navigateToSelectedGame.value = gameProperty
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayGameDetailsComplete() {
        _navigateToSelectedGame.value = null
    }

}
