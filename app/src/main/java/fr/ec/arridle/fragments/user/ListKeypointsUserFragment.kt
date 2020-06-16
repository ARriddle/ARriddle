package fr.ec.arridle.fragments.user

import android.content.Context
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
import fr.ec.arridle.network.KeypointProperty
import fr.ec.arridle.network.SolveProperty

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

        // On met à jour les points clé avec un éventuelle validation
        val sharedPref = activity?.getSharedPreferences(
            "connection",
            Context.MODE_PRIVATE
        )
        val gameId = sharedPref?.getString("game_id", null)
        val userId = sharedPref?.getInt("user_id", -1)


        viewModel.solves.observe(viewLifecycleOwner, Observer {
            val solves : List<SolveProperty>? = viewModel.solves.value?. filter { it.userId == userId && it.gameId == gameId }
            val keypoints: List<KeypointProperty>? = viewModel.properties.value
            Log.i("azer", solves.toString())
            Log.i("azer", keypoints.toString())
            keypoints?.forEach { it.isValidate =
                solves?.any { keypoint -> it.id == keypoint.keypointId }!!
            }

        })

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

        binding.itemsswipetorefresh.setOnRefreshListener {
            viewModel.getKeypointsProperties()
            viewModel.getSolvesProperties()
            val solves : List<SolveProperty>? = viewModel.solves.value?. filter { it.userId == userId && it.gameId == gameId }
            val keypoints: List<KeypointProperty>? = viewModel.properties.value
            keypoints?.forEach { it.isValidate =
                solves?.any { keypoint -> it.id == keypoint.keypointId }!!
            }
            binding.itemsswipetorefresh.isRefreshing = false
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}