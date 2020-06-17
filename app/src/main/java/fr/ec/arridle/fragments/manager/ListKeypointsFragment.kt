package fr.ec.arridle.fragments.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fr.ec.arridle.adapters.KeypointAdapter
import fr.ec.arridle.databinding.FragmentListKeypointsBinding


class ListKeypointsFragment : Fragment() {

    private val viewModel: ListKeypointsViewModel by lazy {
        ViewModelProvider(this).get(ListKeypointsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListKeypointsBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.keypointsView.adapter =
            KeypointAdapter(KeypointAdapter.OnClickListener { //viewModel.displayKeypointDetails(it)
            })

/*
        viewModel.navigateToSelectedKeypoint.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    ListKeypointsFragmentDirections.actionListKeypointsFragmentToCreateKeypointFragment(
                    )
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayKeypointDetailsComplete()
            }
        })
        */

        binding.chessKeypointPicture.setOnClickListener {
            this.findNavController().navigate(
                ListKeypointsFragmentDirections.actionListKeypointsFragmentToCreateKeypointFragment(null)
            )
        }

        binding.itemsswipetorefresh.setOnRefreshListener {
            viewModel.getKeypointsProperties()
            binding.itemsswipetorefresh.isRefreshing = false
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}