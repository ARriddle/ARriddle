package fr.ec.arridle.fragments.manager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import fr.ec.arridle.databinding.FragmentCreateKeypointBinding
import kotlinx.coroutines.runBlocking

class CreateKeypointFragment : Fragment() {
    private var coordinates = LatLng(50.61423, 3.13747)
    private val viewModel: CreateKeypointViewModel by lazy {
        ViewModelProvider(this).get(CreateKeypointViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateKeypointBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        val sharedPref = activity?.getSharedPreferences(
            "connection",
            Context.MODE_PRIVATE
        )
        val gameId = sharedPref?.getString("game_id", null)
        binding.chestImage.setOnClickListener {
            val name = binding.ediTextName.text.toString()
            val description = binding.editTextDescription.text.toString()
            val solution = binding.editTextSolution.text.toString()
            val points = binding.editTextPoints.text.toString().toInt()
            runBlocking {
                viewModel.postKeypointProperties(
                    name = name,
                    description = description,
                    solution = solution,
                    points = points,
                    gameId = gameId!!,
                    latitude = coordinates.latitude,
                    longitude = coordinates.longitude
                )
            }
            view?.findNavController()?.navigateUp()

        }

        val givenCoords = arguments?.get("coordinates")

        if (givenCoords != null) {
            coordinates = givenCoords as LatLng
        }

        return binding.root
    }
}