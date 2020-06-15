package fr.ec.arridle.fragments.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import fr.ec.arridle.activities.MainActivity
import fr.ec.arridle.databinding.FragmentProfileBinding
import kotlinx.coroutines.runBlocking


class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Called to recreate the navdrawer
        (activity as MainActivity).createNavDrawer()
        val sharedPref = activity?.getSharedPreferences("connection", Context.MODE_PRIVATE)

        binding.buttonLogout.setOnClickListener {
            runBlocking {
                viewModel.deleteUserProperties(
                    sharedPref?.getString("game_id", null),
                    sharedPref?.getInt("user_id", -1)
                )
            }
            with(sharedPref?.edit()) {
                this?.putString("status", null)
                this?.putString("game_id", null)
                this?.apply()

                val action =
                    ProfileFragmentDirections.actionProfileFragmentToMainFragment("refresh")
                view?.findNavController()?.navigate(action)
            }
        }

        binding.showPseudo.setOnClickListener {
            binding.editShowPseudo.visibility = View.VISIBLE
            binding.showPseudo.visibility = View.GONE
            binding.editShowPseudo.requestFocus()
            val imm = (activity as MainActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editShowPseudo, 0)


        }
        binding.editShowPseudo.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.showPseudo.text = binding.editShowPseudo.text.toString()
                binding.editShowPseudo.visibility = View.GONE
                binding.showPseudo.visibility = View.VISIBLE
                val imm = (activity as MainActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)

                runBlocking {
                    viewModel.putUserProperties(
                        sharedPref?.getString("game_id", null),
                        sharedPref?.getInt("user_id", -1),
                        binding.showPseudo.text.toString()
                    )
                }
                return@OnKeyListener true
            }
            false
        })

        // Inflate the layout for this fragment
        return binding.root
    }

}