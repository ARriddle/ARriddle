package fr.ec.arridle.fragments.user

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.ec.arridle.network.API
import fr.ec.arridle.network.UserProperty
import kotlinx.coroutines.*


class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    // The internal MutableLiveData String that stores the most recent response
    private val _property = MutableLiveData<UserProperty>()
    private val _rank = MutableLiveData<Int>()


    // The external immutable LiveData for the response String

    val property: LiveData<UserProperty>
        get() = _property

    val rank: LiveData<Int>
        get() = _rank

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(
        viewModelJob + Dispatchers.Main
    )

    init {
        getUsersProperties()
    }

    private fun getUsersProperties() {
        coroutineScope.launch {
            val sharedPref = getApplication<Application>().getSharedPreferences("connection",
                Context.MODE_PRIVATE)
            val gameId = sharedPref.getString("game_id", null)
            val userId = sharedPref.getInt("user_id", -1)
            val users = API.retrofitService.getUsersAsync(game_id = gameId!!)

            try {
                val listResult = users.await()
                val user = findUserById(listResult, userId)
                val place = findRankById(listResult, user)
                _property.value = user
                _rank.value = place
                Log.i("azer", _property.value!!.name)
            } catch (e: Exception) {
                _rank.value = null
                _property.value = null
                e.printStackTrace()
            }
        }
    }
    suspend fun deleteUserProperties(gameId: String?, userId: Int?) {
        coroutineScope {
            launch {
                val post =
                    API.retrofitService.deleteUserAsync(game_id = gameId!!, user_id = userId!!)
                try {
                    post.await()
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

    private fun findUserById(list: List<UserProperty>, userId: Int?) : UserProperty?{
        return list.find {it.id == userId}
    }

    private fun findRankById(list: List<UserProperty>, user: UserProperty?): Int {
        return list.count {it.points > (user?.points ?: 10)} + 1
    }
}