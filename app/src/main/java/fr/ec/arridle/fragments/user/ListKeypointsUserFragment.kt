package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        binding.keypointsView.adapter =
            KeypointAdapter(KeypointAdapter.OnClickListener { viewModel.displayKeypointDetails(it) })


        viewModel.navigateToSelectedKeypoint.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    ListKeypointsUserFragmentDirections.actionListKeypointsFragmentUserToKeypointFragment(
                        it.id
                    )
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayKeypointDetailsComplete()
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

}