package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import fr.ec.arridle.R
import fr.ec.arridle.databinding.FragmentShowKeypointBinding

class KeypointFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentShowKeypointBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val keypointId = KeypointFragmentArgs.fromBundle(requireArguments()).keypointId
        val viewModelFactory = KeypointViewModelFactory(keypointId, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(KeypointViewModel::class.java)

        return binding.root
    }

}