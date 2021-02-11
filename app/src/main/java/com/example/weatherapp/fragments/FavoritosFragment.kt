package com.example.weatherapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapters.CiudadesAdapter
import com.example.weatherapp.database.Ciudades
import com.example.weatherapp.database.DataRepository
import com.example.weatherapp.model.CiudadesViewModel
import kotlin.system.exitProcess


class FavoritosFragment : Fragment() {

    var ciudadSeleccionada: Ciudades? = null
    lateinit var recyclerViewLista: RecyclerView

    lateinit var adapter: CiudadesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        var siFavorito = menu.findItem(R.id.favoriteEvent)
        var noFavorito = menu.findItem(R.id.noFavoriteEvent)
        noFavorito.isVisible = false
        siFavorito.isVisible = false

        var menuItemSearch = menu.findItem(R.id.app_bar_search)
        menuItemSearch.setVisible(true)

        var searchView = menuItemSearch.actionView as SearchView
        searchView.queryHint = "Buscar ciudad Favorita"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritosFragment().apply {}
    }
}