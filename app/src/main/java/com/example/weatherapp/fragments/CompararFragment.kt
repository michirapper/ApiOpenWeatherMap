package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.weatherapp.R
import com.example.weatherapp.database.Ciudades


class CompararFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_comparar, container, false)

//        var bundle: Bundle? = Bundle()
//        bundle = arguments
//        var ciudad1 = bundle!!.getString("ciudad1")

        val args: CompararFragmentArgs by navArgs()
        val ciudad1 = args.ciudad1
        val ciudad2 = args.ciudad2

        //var ciudad2 = bundle!!.get("ciudad2")

        Toast.makeText(context, ciudad1, Toast.LENGTH_SHORT).show()
        Toast.makeText(context, ciudad2, Toast.LENGTH_SHORT).show()
       // Toast.makeText(context, ciudad2.toString(), Toast.LENGTH_SHORT).show()
        //ciudades?.get(0.toString())

        return v
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CompararFragment().apply {}

    }
}