package com.mohalim.edokan.ui.seller

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mohalim.edokan.core.utils.LocationUtils
import com.mohalim.edokan.core.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SellerAddMarketplaceActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    val viewModel by viewModels<SellerAddMarketplaceViewModel>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!LocationUtils.isLocationEnabled(this)){
            Toast.makeText(this, "Please enable location", Toast.LENGTH_SHORT).show()
            finish()
        }

        requestLocationPermission()


        setContent{
            SellerAddMarketplaceScreen(viewModel = viewModel)
        }
    }

    override fun onResume() {
        super.onResume()

        observeScreenStatus(this@SellerAddMarketplaceActivity)

    }

    private fun observeScreenStatus(context: Context) {
        lifecycleScope.launch {
            viewModel.screenStatus.collect{
                when(it){
                    is Resource.Loading->{
                    }

                    is Resource.Success<*> ->{
                        Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    is Resource.Error<*> ->{
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }



    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    viewModel.setLat(latitude)
                    viewModel.setLng(longitude)


                } ?: run {
                    // If lastLocation is null, request a new location
                    requestNewLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).apply {
            setWaitForAccurateLocation(false)
            setMaxUpdateDelayMillis(5000)
        }.build()



        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    // Use latitude and longitude as needed
                    viewModel.setLat(latitude)
                    viewModel.setLng(longitude)
                    // Stop updating location after getting the first location
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }, mainLooper)
    }

}




@Composable
fun SellerAddMarketplaceScreen(viewModel: SellerAddMarketplaceViewModel) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var isApproved by remember { mutableStateOf(false) }
    val screenStatus = viewModel.screenStatus.collectAsState()

    val city by viewModel.city.collectAsState("")
    val phoneNumber by viewModel.phoneNumber.collectAsState("")

    val lat by viewModel.lat.collectAsState()
    val lng by viewModel.lng.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Marketplace Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = "$lat",
            onValueChange = {  },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = "$lng",
            onValueChange = {  },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))


        if (screenStatus.value is Resource.Loading){
            LinearProgressIndicator()
        }


        Button(
            onClick = {
                viewModel.addMarketplace(
                    name.text,
                    lat ,
                    lng,
                    1,
                    "",
                    phoneNumber!!,
                    isApproved
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add Marketplace")
        }
    }
}