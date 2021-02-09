package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.R
import com.example.weatherapp.pojo.City
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main_weather.*
import org.json.JSONException


class MainWeather : Fragment() {
    var API_CODE = "be50e7e09e0d0672fecca55e9b58d2fc"
    var lang = "es"
    private var lista= MutableLiveData <ArrayList<City>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_main_weather, container, false)
        var imageView =  v.findViewById<ImageView>(R.id.imageView)

        getDataWeather(v)
        Picasso.get().load("https://openweathermap.org/img/w/10d.png").into(imageView);

        return v
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    private fun getDataWeather(v: View){
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Madrid&lang="+ lang + "&appid=" + API_CODE


        val requestQueue = Volley.newRequestQueue(requireContext())
        var icon: String = ""
        var description: String = ""
        var temperature = v.findViewById<TextView>(R.id.textView_temperature)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try{
                    var listaUsuarios = ArrayList<City>()
                    //for (contador in 0 until response.length()){
                       // val readData = response.getJSONObject(contador)
//                        var weather = readData.optJSONArray("weather")
//                        for (i in 0 until weather.length()) {
//                            var weather = weather.getJSONObject(i)
//                            icon = weather.getString("icon")
//                            description = weather.getString("description")
//
//
//                        }

                    temperature.text = url

//                        val body = readData.getString("body")
//                        listaUsuarios.add(City(icon, name, description, temperature, feels_like, temp_Max, temp_Min, humedad))
                  //  }
                   // lista.value = listaUsuarios
                }
                catch(e: JSONException){
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error->
                error.printStackTrace()
            })

        requestQueue.add(request)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainWeather().apply{}
    }


}