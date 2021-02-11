package com.example.weatherapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapters.CiudadesAdapter
import com.example.weatherapp.database.Ciudades
import com.example.weatherapp.database.DataRepository
import com.example.weatherapp.model.CiudadesViewModel


class FavoritosFragment : Fragment() {

    var ciudadSeleccionada: Ciudades? = null
    lateinit var recyclerViewLista: RecyclerView

    lateinit var adapter: CiudadesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_favoritos, container, false)

        recyclerViewLista = v.findViewById<RecyclerView>(R.id.recyclerviewlista)
        rellenarListaCiudades()

        return v
    }

    fun rellenarListaCiudades(){
        var preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        var user = preferences?.getString("user", "")
        var dataRepository = DataRepository(requireContext())
        var peliculas = dataRepository.getCiudades(user.toString())

        var viewModel = ViewModelProvider(requireActivity()).get(CiudadesViewModel::class.java)

        adapter = CiudadesAdapter(peliculas[0].ciudades, viewModel)
        recyclerViewLista.setAdapter(adapter)
        recyclerViewLista.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false))
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritosFragment().apply {}
    }
}