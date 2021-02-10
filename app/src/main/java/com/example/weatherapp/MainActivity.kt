package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.weatherapp.database.DataRepository
import com.google.android.material.appbar.CollapsingToolbarLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val layout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

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
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }
}