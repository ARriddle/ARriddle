package fr.ec.arridle.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.adapters.GameAdapter
import fr.ec.arridle.databinding.FragmentMainBinding
import fr.ec.arridle.randomPseudo
import kotlinx.coroutines.runBlocking

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

        (activity as MainActivity).createNavDrawer()

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.gameList.adapter =
            GameAdapter(GameAdapter.OnClickListener { viewModel.displayGameDetails(it) })
        viewModel.navigateToSelectedGame.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                val pseudo = randomPseudo()
                val sharedPref =
                    activity?.getSharedPreferences("connection", Context.MODE_PRIVATE)
                with(sharedPref?.edit()) {
                    try {
                        runBlocking {
                            viewModel.postUserProperties(it.id, pseudo)
                        }
                        this?.putString("status", "user")
                        this?.putString("game_id", it.id)
                        viewModel.userId.value?.let { it1 -> this?.putInt("user_id", it1) }
                        this?.apply()
                    } catch (e: java.lang.Exception) {
                    }
                }

                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToGameFragment(arg1="refresh"
                    )
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayGameDetailsComplete()
            }
        })

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
