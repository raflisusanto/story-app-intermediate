package com.example.storyappsubmission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.remote.response.ListStoryItem

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storyappsubmission.databinding.ActivityMapsBinding
import com.example.storyappsubmission.helper.showToast
import com.example.storyappsubmission.viewmodel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsViewModel.feedbackToast.observe(this) { errorMessage ->
            errorMessage?.showToast(this)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mapsViewModel.listStories.observe(this) { stories ->
            setMarkers(stories)
        }
    }

    private fun setMarkers(data: List<ListStoryItem>) {
        data.forEach { item ->
            if (item.lat != null && item.lon != null) {
                val latLng = LatLng(item.lat, item.lon)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(item.name)
                        .snippet(item.description)
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1f))
            }
        }
    }
}