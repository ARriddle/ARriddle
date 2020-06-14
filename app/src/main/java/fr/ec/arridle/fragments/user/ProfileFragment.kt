package fr.ec.arridle.fragments.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.databinding.FragmentProfileBinding
import kotlinx.coroutines.runBlocking


class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Called to recreate the navdrawer
        (activity as MainActivity).createNavDrawer()

        binding.buttonLogout.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences("connection", Context.MODE_PRIVATE)
            runBlocking {
                viewModel.deleteUserProperties(
                    sharedPref?.getString("game_id", null),
                    sharedPref?.getInt("user_id", -1)
                )
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

}