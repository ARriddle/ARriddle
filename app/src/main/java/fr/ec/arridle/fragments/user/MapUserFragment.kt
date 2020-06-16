package fr.ec.arridle.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import fr.ec.arridle.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

val defaultPosition = LatLng(50.61423, 3.13747)

class MapUserFragment : Fragment() {
    private var firstLoad = true
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val viewModel: ListKeypointsUserViewModel by lazy {
        ViewModelProvider(this).get(ListKeypointsUserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("dbg", "map open")

        val view = inflater.inflate(R.layout.fragment_map_user, container, false)

        val mapView = view.findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView.getMapAsync { googleMap ->
            coroutineScope.launch {
                viewModel.getKeypointsProperties().forEach { kp ->
                    val pos = LatLng(
                        kp.latitude ?: defaultPosition.latitude,
                        kp.longitude ?: defaultPosition.longitude
                    )

                    googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(kp.name)
                    )
                }

                // 50px of margin
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(getMapBounds(), 50)

                if (firstLoad) {
                    googleMap.animateCamera(cameraUpdate)
                    firstLoad = false
                } else {
                    googleMap.moveCamera(cameraUpdate)
                }
            }

            googleMap.setOnInfoWindowClickListener { marker ->
                coroutineScope.launch {
                    viewModel.getKeypointsProperties().forEach {
                        if (it.name == marker.title) {
                            viewModel.displayKeypointDetails(it)
                        }
                    }
                }
            }
        }

        viewModel.navigateToSelectedKeypoint.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(
                    MapUserFragmentDirections.actionMapUserFragmentToKeypointFragment(it.id)
                )

                viewModel.displayKeypointDetailsComplete()
            }
        })

        return view
    }

    private suspend fun getMapBounds(): LatLngBounds {
        var bounds = LatLngBounds.builder()

        viewModel.getKeypointsProperties().forEach {
            val pos = LatLng(
                it.latitude ?: 0.0,
                it.longitude ?: 0.0
            )

            bounds = bounds.include(pos)
        }

        return bounds.build()
    }
}