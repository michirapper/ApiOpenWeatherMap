package com.example.weatherapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.R
import com.example.weatherapp.database.Ciudades
import com.example.weatherapp.database.DataRepository
import com.example.weatherapp.model.CiudadesViewModel
import com.example.weatherapp.pojo.City
import com.squareup.picasso.Picasso
import org.json.JSONException
import kotlin.system.exitProcess


class MainWeather : Fragment() {
    val API_CODE = "be50e7e09e0d0672fecca55e9b58d2fc"
    val lang = "es"
    val units = "metric"
    private var lista = MutableLiveData<ArrayList<City>>()
    lateinit var nombreCiudad: String
    lateinit var siFavorito: MenuItem
    lateinit var noFavorito: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        //get bundle
        var viewModel = ViewModelProvider(requireActivity()).get(CiudadesViewModel::class.java)
        var ciudad = viewModel.getCiudadSeleccionada()

        if (ciudad.nombre != "") {
            nombreCiudad = ciudad.nombre
        } else {
            nombreCiudad = "Cadiz"
        }

       // getActivity()?.supportFragmentManager?.popBackStack()

        getDataWeather(v, nombreCiudad)
        lista.observe(viewLifecycleOwner, Observer {
            //var cuantos = lista.value!!.size
            nombreCiudad = lista.value!!.get(0).name

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

    override fun onResume() {
        var viewModel = ViewModelProvider(requireActivity()).get(CiudadesViewModel::class.java)
        viewModel.setCiudadSeleccionada(Ciudades("Barcelona"))
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        var preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        var user = preferences?.getString("user", "")
        val dataRepository = DataRepository(requireContext())
        var favorito = dataRepository.isFavoritos(user.toString(), nombreCiudad)
        siFavorito = menu.findItem(R.id.favoriteEvent)
        noFavorito = menu.findItem(R.id.noFavoriteEvent)
        if (favorito){
            noFavorito.isVisible = true
            siFavorito.isVisible = false
        }else if (!favorito){
            siFavorito.isVisible = true
            noFavorito.isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)

    }

    private fun getDataWeather(v: View, nombreCiudad: String) {
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=" + nombreCiudad + "&lang=" + lang + "&units=" + units + "&appid=" + API_CODE

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dataRepository = DataRepository(requireContext())
        var preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        var user = preferences?.getString("user", "")
        var favorito = dataRepository.isFavoritos(user.toString(), nombreCiudad)

        return when (item.itemId) {
            R.id.homeFragment -> {
                //Toast.makeText(context, "click on Cerrar sesion", Toast.LENGTH_LONG).show()
                var preferences = activity?.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
                var editor: SharedPreferences.Editor = preferences!!.edit()
                editor.putString("remember", "false")
                editor.apply()
                findNavController().navigate(R.id.nav_host_fragment)
                true
            }
            R.id.favoriteEvent -> {
                if (user != null) {
                    dataRepository.insertCiudad(Ciudades(nombreCiudad), user)
                    if (!favorito) {
                        noFavorito.isVisible = true
                        siFavorito.isVisible = false
                    }
                }
                //Toast.makeText(context, user, Toast.LENGTH_LONG).show()
                true
            }
            R.id.noFavoriteEvent -> {
                if (user != null) {
                    dataRepository.borrarCiudad(user, nombreCiudad)
                    if (favorito) {
                        siFavorito.isVisible = true
                        noFavorito.isVisible = false

                    }
                }
                //Toast.makeText(context, user, Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainWeather().apply {}
    }


}