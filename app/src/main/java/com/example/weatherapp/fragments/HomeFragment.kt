package com.example.weatherapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import kotlin.system.exitProcess


class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Exit")
                builder.setMessage("Are You Sure?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    dialog.dismiss()
                    exitProcess(-1)
                }
                builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val editor: SharedPreferences.Editor = activity?.getSharedPreferences("ciudadEscogida", MODE_PRIVATE)!!.edit()
        editor.clear().apply()
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_home, container, false)
        var buttonLogin = v.findViewById<Button>(R.id.buttonLogin)
        var buttonRegister = v.findViewById<Button>(R.id.buttonRegister)
        var preferences = activity?.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
        var checkBox = preferences?.getString("remember", "")
        if (checkBox.equals("true")) {
            findNavController().navigate(R.id.action_homeFragment_to_mainWeather)
        } else if (checkBox.equals("false")) {
            Toast.makeText(context, "Please Sign in", Toast.LENGTH_SHORT).show()
        }

        buttonLogin.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
//            findNavController().navigate(action)

            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        buttonRegister.setOnClickListener {
           // val action = HomeFragmentDirections.actionHomeFragmentToRegisterFragment()
            findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
        }


        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
            }
    }

}