package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.adapters.KeypointAdapter
import fr.ec.arridle.databinding.FragmentShowGameBinding

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this).get(GameViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShowGameBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.keypointsView.adapter =   KeypointAdapter(KeypointAdapter.OnClickListener { })
        (activity as MainActivity).createNavDrawer()
        return binding.root
    }
}