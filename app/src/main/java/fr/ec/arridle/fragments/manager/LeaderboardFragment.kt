package fr.ec.arridle.fragments.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.ec.arridle.adapters.UserAdapter
import fr.ec.arridle.databinding.FragmentLeaderboardBinding
import fr.ec.arridle.databinding.FragmentLeaderboardUserBinding

class LeaderboardFragment : Fragment() {

    private val viewModel: LeaderboardViewModel by lazy {
        ViewModelProvider(this).get(LeaderboardViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLeaderboardBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.leaderboard.adapter = UserAdapter()

        // Inflate the layout for this fragment
        return binding.root
    }
}