package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object{
        private val REQUEST_PERMISSION_REQUEST_CODE = 2020
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val layout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_PERMISSION_REQUEST_CODE)
        }else {
            getCurrentLocation()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.homeFragment -> {
                // Toast.makeText(applicationContext, "click on Cerrar sesion", Toast.LENGTH_LONG).show()
                var preferences = this?.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
                var editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("remember", "false")
                editor.apply()
                val navController = findNavController(R.id.nav_host_fragment)
                item.onNavDestinationSelected(navController)
                true
            }
            R.id.favoritosFragment -> {
                val navController = findNavController(R.id.nav_host_fragment)
                return item.onNavDestinationSelected(navController)

//                val navController = findNavController(R.id.favoritosFragment)
//                item.onNavDestinationSelected(navController)
                //findNavController().navigate(R.id.viewTransactionsAction)

                true
            }
            R.id.mainWeather -> {
                val navController = findNavController(R.id.nav_host_fragment)
                return item.onNavDestinationSelected(navController)

//                val navController = findNavController(R.id.favoritosFragment)
//                item.onNavDestinationSelected(navController)
                //findNavController().navigate(R.id.viewTransactionsAction)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Location

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_REQUEST_CODE && grantResults.size > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }else{
                Toast.makeText(this@MainActivity,"Permission Denied!",Toast.LENGTH_SHORT).show()
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {

        val preferences = this.getSharedPreferences("coordenadas", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = preferences!!.edit()

        var locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        var latitude = 0.0
        var longitude = 0.0

        //now getting latitude and longitude

        if (checkPermissions()){
            LocationServices.getFusedLocationProviderClient(this@MainActivity)
                .requestLocationUpdates(locationRequest,object : LocationCallback(){
                    override fun onLocationResult(locationResult: LocationResult?) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(this@MainActivity)
                            .removeLocationUpdates(this)
                        if (locationResult != null && locationResult.locations.size > 0){
                            var locIndex = locationResult.locations.size-1

                            latitude = locationResult.locations.get(locIndex).latitude
                            longitude = locationResult.locations.get(locIndex).longitude

                           // Toast.makeText(applicationContext, "Latitud y longitud: ${latitude} : ${longitude}", Toast.LENGTH_SHORT).show()

                            editor.putString("latitude", latitude.toString())
                            editor.putString("longitude", longitude.toString())
                            editor.apply()


//                            tvLatitude.text = "Latitude: "+latitude
//                            tvLongitude.text = "Longitude: "+longitude
                        }
                    }
                }, Looper.getMainLooper())
        }

    }
    fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}