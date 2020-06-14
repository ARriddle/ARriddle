package fr.ec.arridle.fragments.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import fr.ec.arridle.R
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.databinding.FragmentProfileBinding
import fr.ec.arridle.network.API
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile, container, false
        )
        (activity as MainActivity).createNavDrawer()

        binding.buttonLogout.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences("connection", Context.MODE_PRIVATE)
            runBlocking {
                deleteUserProperties(
                    sharedPref?.getString("game_id", null),
                    sharedPref?.getInt("user_id", -1))
            }
            with(sharedPref?.edit()) {
                this?.putString("status", null)
                this?.putString("game_id", null)
                this?.apply()

                val action =
                    ProfileFragmentDirections.actionProfileFragmentToMainFragment("refresh")
                view?.findNavController()?.navigate(action)
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private suspend fun deleteUserProperties(gameId: String?, userId: Int?) {
        coroutineScope {
            launch {
                val post = API.retrofitService.deleteUserAsync(game_id = gameId!!, user_id = userId!!)
                try {
                    val user = post.await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}