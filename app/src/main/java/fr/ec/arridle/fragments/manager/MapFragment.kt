package fr.ec.arridle.fragments.manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import fr.ec.arridle.R
import fr.ec.arridle.fragments.user.ListKeypointsUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

val defaultPosition = LatLng(50.61423, 3.13747)

class MapFragment : Fragment() {
    private var firstLoad = true
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val viewModel: ListKeypointsUserViewModel by lazy {
        ViewModelProvider(this).get(ListKeypointsUserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

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
                // 50px of margin
                val bounds = setupMap(googleMap)
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50)

                if (firstLoad) {
                    googleMap.animateCamera(cameraUpdate)
                    firstLoad = false
                } else {
                    googleMap.moveCamera(cameraUpdate)
                }
            }

            googleMap.setOnMapLongClickListener {
                findNavController().navigate(
                    MapFragmentDirections.actionMapFragmentToCreateKeypointFragment(it)
                )
            }
        }

        return view
    }

    /* Adds markers for keypoints and returns the bounds that encompass them.
     */
    private suspend fun setupMap(map: GoogleMap): LatLngBounds {
        var bounds = LatLngBounds.builder()
        var hasKeypoints = false

        viewModel.getKeypointsProperties().forEach { kp ->
            hasKeypoints = true

            val pos = LatLng(
                kp.latitude ?: defaultPosition.latitude,
                kp.longitude ?: defaultPosition.longitude
            )

            bounds = bounds.include(pos)
            map.addMarker(
                MarkerOptions()
                    .position(pos)
                    .title(kp.name)
            )
        }

        if (!hasKeypoints)
            bounds = bounds.include(defaultPosition)

        return bounds.build()
    }
}
