package fr.ec.arridle.fragments.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import fr.ec.arridle.R
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.databinding.FragmentJoinGameBinding
import fr.ec.arridle.network.API
import fr.ec.arridle.network.GameProperty
import kotlinx.coroutines.*


class JoinGameFragment : Fragment() {
    private var job = Job()
    private val coroutineScope = CoroutineScope(
        job + Dispatchers.Main
    )

    // The internal MutableLiveData String that stores the most recent response
    private val _properties = MutableLiveData<GameProperty>()

    // The external immutable LiveData for the response String

    val properties: LiveData<GameProperty>
        get() = _properties

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentJoinGameBinding>(
            inflater,
            R.layout.fragment_join_game, container, false
        )
        (activity as MainActivity).createNavDrawer()
        binding.buttonJoinGame.setOnClickListener {
            val id = binding.editTextIdGame.text.toString()
            if (validate(id)) {
                runBlocking {
                    getGameProperties(id)
                }
                if (properties.value != null){
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref?.edit()) {
                        this?.putString("status","user")
                        this?.commit()
                    }

                    val action = JoinGameFragmentDirections.actionJoinFragmentToGameFragment("refresh")
                    view?.findNavController()?.navigate(action)
                }
                else {
                    binding.textError.text = getString(R.string.error_game_id_doesnt_exist)
                }
            } else {
                binding.textError.text = getString(R.string.error_game_id_format)
            }
        }

        return binding.root
    }

    private fun validate(id: String): Boolean {
        val pattern = "^[A-Z0-9]{8}$".toRegex()
        return pattern.matches(id)
    }

    private suspend fun getGameProperties(id: String) {
        coroutineScope {
            launch {
                val getPropertiesDeferred = API.retrofitService.getGameAsync(id)

                try {
                    val game = getPropertiesDeferred.await()
                    _properties.value = game
                } catch (e: Exception) {
                    _properties.value = null
                }
            }
        }
    }
}