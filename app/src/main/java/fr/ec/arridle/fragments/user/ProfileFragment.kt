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
            with (sharedPref?.edit()) {
                this?.putString("status", null)
                this?.putString("game_id", null)
                this?.apply()

                val action = ProfileFragmentDirections.actionProfileFragmentToMainFragment("refresh")
                view?.findNavController()?.navigate(action)
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}