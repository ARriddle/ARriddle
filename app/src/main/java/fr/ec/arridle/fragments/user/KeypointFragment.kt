package fr.ec.arridle.fragments.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.ec.arridle.databinding.FragmentShowKeypointBinding
import kotlinx.coroutines.runBlocking

class KeypointFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentShowKeypointBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val keypointId = KeypointFragmentArgs.fromBundle(requireArguments()).keypointId
        val viewModelFactory = KeypointViewModelFactory(keypointId, application)
        binding.viewModel =
            ViewModelProvider(this, viewModelFactory).get(KeypointViewModel::class.java)

        binding.buttonSend.setOnClickListener {
            val sharedPref = activity?.getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref?.getString("game_id", null)
            val userId = sharedPref?.getInt("user_id", -1)
            val hasBeenResolved = ((binding.viewModel as KeypointViewModel).solves.value?.filter { it.keypointId == keypointId && it.userId == userId }).isNullOrEmpty()
            if (hasBeenResolved) {
                Toast.makeText(
                    activity,
                    "Vosu avez déjà résolu le point d'intérêt !",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (binding.editTextAnswer.text.toString() == (binding.viewModel as KeypointViewModel).keypointProperty.value!!.solution) {
                    val points =
                        (binding.viewModel as KeypointViewModel).user.value!!.points + (binding.viewModel as KeypointViewModel).keypointProperty.value!!.points

                    runBlocking {
                        (binding.viewModel as KeypointViewModel).putUserProperties(
                            gameId,
                            userId,
                            points
                        )
                        (binding.viewModel as KeypointViewModel).postSolve(
                            gameId = gameId,
                            userId = userId,
                            keypointId = keypointId
                        )

                    }
                    Toast.makeText(
                        activity,
                        "Bravo vous marquez ${(binding.viewModel as KeypointViewModel).keypointProperty.value!!.points} points",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        activity,
                        "Ce n'est pas la réponse attendue.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        return binding.root
    }

}