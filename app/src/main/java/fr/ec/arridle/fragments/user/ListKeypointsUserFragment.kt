package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.ec.arridle.adapters.KeypointAdapter
import fr.ec.arridle.databinding.FragmentListKeypointsUserBinding

class ListKeypointsUserFragment : Fragment() {

    private val viewModel: ListKeypointsUserViewModel by lazy {
        ViewModelProvider(this).get(ListKeypointsUserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListKeypointsUserBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.keypointsView.adapter = KeypointAdapter()

        // Inflate the layout for this fragment
        return binding.root
    }

}