package fr.ec.arridle.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import fr.ec.arridle.adapters.GameAdapter

import fr.ec.arridle.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.gameList.adapter = GameAdapter()

        binding.cardViewJoinGame.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToJoinFragment()
            view?.findNavController()?.navigate(action)
        }

        binding.cardViewCreateGame.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToCreateGameFragment()
            view?.findNavController()?.navigate(action)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}
