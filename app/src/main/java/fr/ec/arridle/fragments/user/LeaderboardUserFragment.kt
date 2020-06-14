package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.ec.arridle.R
import fr.ec.arridle.adapters.UserAdapter
import fr.ec.arridle.databinding.FragmentLeaderboardBinding
import fr.ec.arridle.databinding.FragmentLeaderboardUserBinding
import fr.ec.arridle.fragments.manager.LeaderboardViewModel

class LeaderboardUserFragment : Fragment() {

    private val viewModel: LeaderboardUserViewModel by lazy {
        ViewModelProvider(this).get(LeaderboardUserViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLeaderboardUserBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.leaderboard.adapter = UserAdapter()

        // Inflate the layout for this fragment
        return binding.root
    }
}