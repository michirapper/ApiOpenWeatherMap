package com.example.weatherapp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.R
import com.example.weatherapp.pojo.City
import com.squareup.picasso.Picasso
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException


class CompararFragment : Fragment() {

    val API_CODE = "be50e7e09e0d0672fecca55e9b58d2fc"
    val lang = "es"
    val units = "metric"
    private var lista1 = MutableLiveData<ArrayList<City>>()
    private var lista2 = MutableLiveData<ArrayList<City>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_comparar, container, false)





        val args: CompararFragmentArgs by navArgs()
        val ciudad1 = args.ciudad1
        val ciudad2 = args.ciudad2

        //var ciudad2 = bundle!!.get("ciudad2")

        Toast.makeText(context, ciudad1, Toast.LENGTH_SHORT).show()
        Toast.makeText(context, ciudad2, Toast.LENGTH_SHORT).show()

        getCiudad1(v, ciudad1, 1)
        getCiudad2(v, ciudad2, 2)




        return v
    }



    private fun getDataWeather(nombreCiudad: String, numCiudad: Int) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=" + nombreCiudad + "&lang=" + lang + "&units=" + units + "&appid=" + API_CODE

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
                    listaCity.clear()

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

                    listaCity.add(City(icon,name,description,temperature,feels_like,temp_Max,temp_Min,humedad))
                    if (numCiudad == 1){
                        lista1.value = listaCity
                    }
                    if (numCiudad == 2){
                        lista2.value = listaCity
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(context, getVolleyError(error), Toast.LENGTH_LONG).show()
            })

        requestQueue.add(request)
    }

    fun getVolleyError(error: VolleyError): String {
        var errorMsg = ""
        if (error is NoConnectionError) {
            val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork: NetworkInfo? = null
            activeNetwork = cm.activeNetworkInfo
            errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                "Server is not connected to the internet. Please try again"
            } else {
                "Your device is not connected to internet.please try again with active internet connection"
            }
        } else if (error is NetworkError || error.cause is ConnectException) {
            errorMsg = "Your device is not connected to internet.please try again with active internet connection"
        } else if (error.cause is MalformedURLException) {
            errorMsg = "That was a bad request please try again…"
        } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
            errorMsg = "There was an error parsing data…"
        } else if (error.cause is OutOfMemoryError) {
            errorMsg = "Device out of memory"
        } else if (error is AuthFailureError) {
            errorMsg = "Failed to authenticate user at the server, please contact support"
        } else if (error is ServerError || error.cause is ServerError) {
            errorMsg = "Internal server error occurred please try again...."
        } else if (error is TimeoutError || error.cause is SocketTimeoutException || error.cause is ConnectTimeoutException || error.cause is SocketException || (error.cause!!.message != null && error.cause!!.message!!.contains(
                "Your connection has timed out, please try again"
            ))
        ) {
            errorMsg = "Your connection has timed out, please try again"
        } else {
            errorMsg = "An unknown error occurred during the operation, please try again"
        }
        return errorMsg
    }

    fun getCiudad1(v: View, ciudad: String, numCiudad: Int){
        var icon1 = v.findViewById<ImageView>(R.id.imageView1)
        var description1 = v.findViewById<TextView>(R.id.textView_Description1)
        var name1 = v.findViewById<TextView>(R.id.textView_name1)
        var temperature1 = v.findViewById<TextView>(R.id.textView_temperature1)
        var feels_like1 = v.findViewById<TextView>(R.id.textView_feels_like1)
        var temp_Max1 = v.findViewById<TextView>(R.id.textView_tempMax1)
        var temp_Min1 = v.findViewById<TextView>(R.id.textView_tempMin1)
        var humedad1 = v.findViewById<TextView>(R.id.textView_humedad1)

        getDataWeather(ciudad, numCiudad)
        lista1.observe(viewLifecycleOwner, Observer {
            //nombreCiudad = lista.value!!.get(0).name

            val primeraLetra: String = lista1.value!!.get(0).description.substring(0, 1).toUpperCase()
            val restoDeLaCadena: String = lista1.value!!.get(0).description.substring(1)
            val primeraMinuscula = primeraLetra + restoDeLaCadena

            description1.text = primeraMinuscula
            name1.text = lista1.value!!.get(0).name
            temperature1.text = lista1.value!!.get(0).temperature.toString() + "°C"
            feels_like1.text =
                "Se siente como " + lista1.value!!.get(0).feels_like.toString() + "°C"
            temp_Max1.text = "Maximas: " + lista1.value!!.get(0).temp_Max.toString() + "°C"
            temp_Min1.text = "Minimas: " + lista1.value!!.get(0).temp_Min.toString() + "°C"
            humedad1.text = "Humedad: " + lista1.value!!.get(0).humedad.toString() + "%"
            Picasso.get().load("https://openweathermap.org/img/w/" + lista1.value!![0].icon + ".png").into(icon1)
        })
    }
    fun getCiudad2(v: View, ciudad: String, numCiudad: Int){
        var icon2 = v.findViewById<ImageView>(R.id.imageView2)
        var description2 = v.findViewById<TextView>(R.id.textView_Description2)
        var name2 = v.findViewById<TextView>(R.id.textView_name2)
        var temperature2 = v.findViewById<TextView>(R.id.textView_temperature2)
        var feels_like2 = v.findViewById<TextView>(R.id.textView_feels_like2)
        var temp_Max2 = v.findViewById<TextView>(R.id.textView_tempMax2)
        var temp_Min2 = v.findViewById<TextView>(R.id.textView_tempMin2)
        var humedad2 = v.findViewById<TextView>(R.id.textView_humedad2)

        getDataWeather(ciudad, numCiudad)
        lista2.observe(viewLifecycleOwner, Observer {
            //nombreCiudad = lista.value!!.get(0).name

            val primeraLetra: String = lista2.value!!.get(0).description.substring(0, 1).toUpperCase()
            val restoDeLaCadena: String = lista2.value!!.get(0).description.substring(1)
            val primeraMinuscula = primeraLetra + restoDeLaCadena

            description2.text = primeraMinuscula
            name2.text = lista2.value!!.get(0).name
            temperature2.text = lista2.value!!.get(0).temperature.toString() + "°C"
            feels_like2.text =
                "Se siente como " + lista2.value!!.get(0).feels_like.toString() + "°C"
            temp_Max2.text = "Maximas: " + lista2.value!!.get(0).temp_Max.toString() + "°C"
            temp_Min2.text = "Minimas: " + lista2.value!!.get(0).temp_Min.toString() + "°C"
            humedad2.text = "Humedad: " + lista2.value!!.get(0).humedad.toString() + "%"
            Picasso.get().load("https://openweathermap.org/img/w/" + lista2.value!![0].icon + ".png").into(icon2)
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CompararFragment().apply {}

    }
}