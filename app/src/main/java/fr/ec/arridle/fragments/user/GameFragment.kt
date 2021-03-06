package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.adapters.KeypointAdapter
import fr.ec.arridle.databinding.FragmentShowGameBinding
import fr.ec.arridle.network.KeypointProperty
import fr.ec.arridle.network.SolveProperty

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this).get(GameViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).createNavDrawer()

        val binding = FragmentShowGameBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.keypointsView.adapter =
            KeypointAdapter(KeypointAdapter.OnClickListener { viewModel.displayKeypointDetails(it) })
        viewModel.navigateToSelectedKeypoint.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    GameFragmentDirections.actionGameFragmentToKeypointFragment(
                        it.id
                    )
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayKeypointDetailsComplete()
            }
        })

        binding.imageView8.setOnClickListener {
            this.findNavController().navigate(
                GameFragmentDirections.actionGameFragmentToMapUserFragment(
                )
            )
        }

        binding.itemsswipetorefresh.setOnRefreshListener {
            viewModel.getKeypointsProperties()
            binding.itemsswipetorefresh.isRefreshing = false
        }
        return binding.root
    }
}