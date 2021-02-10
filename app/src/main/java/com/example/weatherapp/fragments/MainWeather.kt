package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.R
import com.example.weatherapp.pojo.City
import com.squareup.picasso.Picasso
import org.json.JSONException


class MainWeather : Fragment() {
    val API_CODE = "be50e7e09e0d0672fecca55e9b58d2fc"
    val lang = "es"
    val units = "metric"
    private var lista = MutableLiveData<ArrayList<City>>()

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
        var icon = v.findViewById<ImageView>(R.id.imageView)
        var description = v.findViewById<TextView>(R.id.textView_Description)
        var name = v.findViewById<TextView>(R.id.textView_name)
        var temperature = v.findViewById<TextView>(R.id.textView_temperature)
        var feels_like = v.findViewById<TextView>(R.id.textView_feels_like)
        var temp_Max = v.findViewById<TextView>(R.id.textView_tempMax)
        var temp_Min = v.findViewById<TextView>(R.id.textView_tempMin)
        var humedad = v.findViewById<TextView>(R.id.textView_humedad)


        getDataWeather(v)
        lista.observe(viewLifecycleOwner, Observer {
            //var cuantos = lista.value!!.size

            val primeraLetra: String =
                lista.value!!.get(0).description.substring(0, 1).toUpperCase()
            val restoDeLaCadena: String = lista.value!!.get(0).description.substring(1)
            val primeraMinuscula = primeraLetra + restoDeLaCadena

            description.text = primeraMinuscula
            name.text = lista.value!!.get(0).name
            temperature.text = lista.value!!.get(0).temperature.toString() + "°C"
            feels_like.text = "Se siente como " + lista.value!!.get(0).feels_like.toString() + "°C"
            temp_Max.text = "Maximas: " + lista.value!!.get(0).temp_Max.toString() + "°C"
            temp_Min.text = "Minimas: " + lista.value!!.get(0).temp_Min.toString() + "°C"
            humedad.text = "Humedad: " + lista.value!!.get(0).humedad.toString() + "%"
            Picasso.get().load("https://openweathermap.org/img/w/" + lista.value!![0].icon + ".png")
                .into(
                    icon
                );

        })



        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getDataWeather(v: View) {
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=Madrid&lang=" + lang + "&units=" + units + "&appid=" + API_CODE


        val requestQueue = Volley.newRequestQueue(requireContext())
        var icon: String = ""
        var description: String = ""
        var name: String = ""
        var temperature: Int = 0
        var feels_like: Int = 0
        var temp_Max: Int = 0
        var temp_Min: Int = 0
        var humedad: Int = 0

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    var listaCity = ArrayList<City>()

                    name = response.getString("name")

                    var weather = response.optJSONArray("weather")
                    for (i in 0 until weather.length()) {
                        var weather = weather.getJSONObject(i)
                        icon = weather.getString("icon")
                        description = weather.getString("description")
                    }
                    var main = response.optJSONObject("main")
                    temperature = main.getInt("temp")
                    feels_like = main.getInt("feels_like")
                    temp_Max = main.getInt("temp_max")
                    temp_Min = main.getInt("temp_min")
                    humedad = main.getInt("humidity")


                    // Log.e("my activity", name.toString())

                    listaCity.add(
                        City(
                            icon,
                            name,
                            description,
                            temperature,
                            feels_like,
                            temp_Max,
                            temp_Min,
                            humedad
                        )
                    )

                    lista.value = listaCity
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })

        requestQueue.add(request)
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainWeather().apply {}
    }


}