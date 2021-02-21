package com.example.weatherapp.fragments

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.R
import com.example.weatherapp.database.DataRepository
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException


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
                            Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                            //Toast.makeText(context, "existe", Toast.LENGTH_SHORT).show()
                        }
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

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ModificarFragment().apply {}
    }
}