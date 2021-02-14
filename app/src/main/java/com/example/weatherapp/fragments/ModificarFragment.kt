package com.example.weatherapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.R
import com.example.weatherapp.database.DataRepository
import org.json.JSONException


class ModificarFragment : Fragment() {

    val API_CODE = "be50e7e09e0d0672fecca55e9b58d2fc"
    val lang = "es"
    val units = "metric"
    lateinit var editTextCiudad: EditText
    lateinit var user: String
    lateinit var ciudadOld: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_modificar, container, false)

        editTextCiudad = v.findViewById<EditText>(R.id.editTextNombreCiudadMod)
        var botonModificar = v.findViewById<Button>(R.id.buttonModificar)
        val args: ModificarFragmentArgs by navArgs()
        ciudadOld = args.ciudad
        var preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        user = preferences?.getString("user", "").toString()
        editTextCiudad.setText(ciudadOld)


        botonModificar.setOnClickListener {
            funModificar()
        }

        return v
    }

    fun funModificar(){
        val dataRepository = DataRepository(requireContext())
        var ciudadNew = editTextCiudad.text.toString()
        var code = ""
        val url = "https://api.openweathermap.org/data/2.5/weather?q=" + ciudadNew + "&lang=" + lang + "&units=" + units + "&appid=" + API_CODE
        val requestQueue = Volley.newRequestQueue(requireContext())

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    code = response.getString("cod")
                    ciudadNew = response.getString("name")

                    if (user != null) {
                        if (code == "200") {
                            dataRepository.actualizarCiudad(user, ciudadOld, ciudadNew)
                            //Toast.makeText(context, "existe", Toast.LENGTH_SHORT).show()
                        }
                    }

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
            ModificarFragment().apply {}
    }
}