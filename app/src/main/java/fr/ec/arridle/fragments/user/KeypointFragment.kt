package fr.ec.arridle.fragments.user

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.ec.arridle.databinding.FragmentShowKeypointBinding
import kotlinx.android.synthetic.main.fragment_show_keypoint.*
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

        binding.buttonVocal.setOnClickListener {
            displaySpeechRecognizer()
        }

        binding.buttonSend.setOnClickListener {
            (binding.viewModel as KeypointViewModel).getSolvesProperties()
            val sharedPref = activity?.getSharedPreferences(
                "connection",
                Context.MODE_PRIVATE
            )
            val gameId = sharedPref?.getString("game_id", null)
            val userId = sharedPref?.getInt("user_id", -1)
            Log.i("azer", (binding.viewModel as KeypointViewModel).solves.value.toString())
            val hasBeenResolved =
                !(((binding.viewModel as KeypointViewModel).solves.value?.filter { it.keypointId == keypointId && it.userId == userId }).isNullOrEmpty())

            if (binding.editTextAnswer.text.toString() == (binding.viewModel as KeypointViewModel).keypointProperty.value!!.solution) {
                if (hasBeenResolved) {
                    Toast.makeText(
                        activity,
                        "Vous avez déjà résolu le point d'intérêt !",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

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
                }

            } else {
                Toast.makeText(
                    activity,
                    "Ce n'est pas la réponse attendue.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    /* Taken from https://developer.android.com/training/wearables/apps/voice#FreeFormSpeech
     */
    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }

        kotlin.runCatching {
            startActivityForResult(intent, 0)
        }.onFailure {
            Toast.makeText(activity,
                "Installez l'application Google",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val spokenText: String? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let {
                it?.get(0)
            }

            if (spokenText != null) {
                editTextAnswer.setText(spokenText)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}