package fr.ec.arridle.fragments.manager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import fr.ec.arridle.databinding.FragmentCreateGameBinding
import fr.ec.arridle.stringToEpochtime
import fr.ec.arridle.validateFormatDate
import fr.ec.arridle.validateFormatHour
import kotlinx.coroutines.runBlocking

class CreateGameFragment : Fragment() {
    private val viewModel: CreateGameViewModel by lazy {
        ViewModelProvider(this).get(CreateGameViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateGameBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.buttonCreateGame.setOnClickListener {
            val name = binding.editGameName.text.toString()
            val duration = binding.editDuration.text.toString()
            val nbPlayerMax = binding.editNbPlayers.text.toString().toInt()
            val visibility = binding.publicGame.isChecked
            val timeStart =
                binding.editStartingDate.text.toString() + ":" + binding.editStartingTime.text.toString()
            if (validateFormatDate(timeStart) && validateFormatHour(duration)) {
                val durationInt = convertToSeconds(duration)
                val timeStartInt = stringToEpochtime(timeStart)

                runBlocking {
                    viewModel.postGameProperties(
                        name = name,
                        duration = durationInt,
                        nbPlayerMax = nbPlayerMax,
                        timeStart = timeStartInt,
                        visibility = visibility
                    )
                }
                val sharedPref =
                    activity?.getSharedPreferences("connection", Context.MODE_PRIVATE)
                with(sharedPref?.edit()) {
                    this?.putString("status", "manager")
                    this?.putString("game_id", viewModel.gameId.value)
                    this?.apply()
                }
                val action =
                    CreateGameFragmentDirections.actionCreateGameFragmentToManageGameFragment("refresh")
                view?.findNavController()?.navigate(action)
            } else {
                Toast.makeText(context, "Format incorrect", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun convertToSeconds(date: String): Int {
        return (Integer.parseInt(date[0].toString()) + Integer.parseInt(date[1].toString()) * 3600 +
                Integer.parseInt(date[3].toString()) + Integer.parseInt(date[4].toString()) * 60 +
                Integer.parseInt(date[6].toString()) + Integer.parseInt(date[7].toString()))
    }

}