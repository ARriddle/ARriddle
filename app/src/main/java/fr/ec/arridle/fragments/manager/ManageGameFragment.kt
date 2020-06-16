package fr.ec.arridle.fragments.manager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import fr.ec.arridle.R
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.databinding.FragmentCreateGameBinding
import fr.ec.arridle.databinding.FragmentManageGameBinding
import kotlinx.coroutines.runBlocking

class ManageGameFragment : Fragment() {
    private val viewModel: ManageGameViewModel by lazy {
        ViewModelProvider(this).get(ManageGameViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentManageGameBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        val sharedPref = activity?.getSharedPreferences("connection", Context.MODE_PRIVATE)

        binding.buttonLogout.setOnClickListener { runBlocking {
            viewModel.deleteGameProperties(
                sharedPref?.getString("game_id", null)
            )
        }
            with(sharedPref?.edit()) {
                this?.putString("status", null)
                this?.putString("game_id", null)
                this?.apply()

                val action =
                    ManageGameFragmentDirections.actionManageGameFragmentToMainFragment("refresh")
                view?.findNavController()?.navigate(action)
            } }
        (activity as MainActivity).createNavDrawer()
        // Inflate the layout for this fragment
        return binding.root
    }

}