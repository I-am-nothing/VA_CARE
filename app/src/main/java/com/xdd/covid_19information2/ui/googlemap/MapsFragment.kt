package com.xdd.covid_19information2.ui.googlemap

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.xdd.covid_19information2.ActionBar
import com.xdd.covid_19information2.MainActivity
import com.xdd.covid_19information2.R
import com.xdd.covid_19information2.databinding.ConfirmDialogBinding
import com.xdd.covid_19information2.localdatabase.bodylog.Body
import com.xdd.covid_19information2.method.Covid19Information
import org.json.JSONArray
import java.lang.Exception
import java.util.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var dialog: AlertDialog

    private lateinit var vaccineLocation: JSONArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        (requireActivity() as MainActivity).setActionBar(ActionBar.Cancel, "Choose A Point")

        dialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(R.layout.loading_view)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(23.889771, 120.927195), 9f))
        Covid19Information(requireActivity()).getVaccineLocation {
            if(it != null){
                vaccineLocation = it
                for(i in 0 until it.length()){
                    val location = LatLng(it.getJSONObject(i).getDouble("LocationX"), it.getJSONObject(i).getDouble("LocationY"))
                    requireActivity().runOnUiThread {
                        googleMap.addMarker(MarkerOptions().position(location).title(it.getJSONObject(i).getString("Name")))
                    }
                }
            }
        }

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener {
            val currentLocation = LatLng(it.latitude, it.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14f))
            dialog.dismiss()
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 5000f, locationListener)

        googleMap.setOnMarkerClickListener {
            val position = it.id.substring(1, it.id.length).toInt()
            val view = View.inflate(context, R.layout.confirm_dialog, null)
            val binding = ConfirmDialogBinding.bind(view)

            val dialog = AlertDialog.Builder(context)
                .setView(view)
                .create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            binding.title2Tv.text = "Confirm Location"
            binding.confirm2Btn.text = "Confirm"
            binding.messageTv.text = "Are you sure choosing ${vaccineLocation.getJSONObject(position).getString("Name")}"

            binding.cancel2Btn.setOnClickListener {
                dialog.dismiss()
            }

            binding.confirm2Btn.setOnClickListener {
                dialog.dismiss()
                Navigation.findNavController(requireView()).apply {
                    previousBackStackEntry?.savedStateHandle?.set("VaccineLocation", vaccineLocation.getJSONObject(position).toString())
                    popBackStack()
                }
            }

            false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }
}