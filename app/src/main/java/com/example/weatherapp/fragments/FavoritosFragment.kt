package com.example.weatherapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapters.CiudadesAdapter
import com.example.weatherapp.adapters.RecyclerItemClickListener
import com.example.weatherapp.database.Ciudades
import com.example.weatherapp.database.DataRepository
import com.example.weatherapp.model.CiudadesViewModel
import java.util.*


class FavoritosFragment : Fragment(), ActionMode.Callback {

    var ciudadSeleccionada: Ciudades? = null

    lateinit var recyclerViewLista: RecyclerView

    lateinit var adapter: CiudadesAdapter

    lateinit var ciudades: List<Ciudades>

    private var actionMode: ActionMode? = null

    private var isMultiSelect = false

    private var selectedIds: MutableList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bundle = bundleOf(Pair("ciudad", ""))
                findNavController().navigate(
                    FavoritosFragmentDirections.actionFavoritosFragmentToMainWeather(
                        bundle
                    )
                )
            }

        })


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
        ciudades = dataRepository.getCiudades(user.toString())[0].ciudades

        var viewModel = ViewModelProvider(requireActivity()).get(CiudadesViewModel::class.java)

        adapter = CiudadesAdapter(context, ciudades, viewModel)
        recyclerViewLista.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerViewLista.adapter = adapter
        recyclerViewLista.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                recyclerViewLista,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        if (isMultiSelect) {
                            multiSelect(position)
                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {
                        if (!isMultiSelect) {
                            selectedIds = ArrayList()
                            isMultiSelect = true
                            if (actionMode == null) {
                                actionMode = activity!!.startActionMode(this@FavoritosFragment)
                            }
                        }
                        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
                        multiSelect(position)
                    }
                })
        )


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        var siFavorito = menu.findItem(R.id.favoriteEvent)
        var noFavorito = menu.findItem(R.id.noFavoriteEvent)
        var favorito = menu.findItem(R.id.favoritosFragment)
        var atras = menu.findItem(R.id.mainWeather)
        noFavorito.isVisible = false
        siFavorito.isVisible = false
        favorito.isVisible = false
        atras.isVisible = true

        var menuItemSearch = menu.findItem(R.id.app_bar_search)
        menuItemSearch.setVisible(true)

        var searchView = menuItemSearch.actionView as SearchView
        searchView.queryHint = "Buscar ciudad Favorita"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    private fun multiSelect(position: Int) {
        val data = adapter!!.getItem(position)
        if (data != null) {
            if (actionMode != null) {
                if (selectedIds.contains(data.nombre)) selectedIds.remove(data.nombre) else selectedIds.add(
                    data.nombre
                )
                if (selectedIds.size > 0) actionMode!!.title = selectedIds.size.toString() //show selected item count on action mode.
                else {
                    actionMode!!.title = "" //remove item count from action mode.
                    actionMode!!.finish() //hide action mode.
                }
                adapter!!.setSelectedIds(selectedIds)
            }
        }
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        val inflater = mode.menuInflater
        inflater.inflate(R.menu.menu_comparacion, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.compararFragment -> {
                var ciudadesSelected = mutableListOf<Ciudades>()
                if (selectedIds.size < 3) {
                    //just to show selected items.
                   // val stringBuilder = StringBuilder()
                    for (data in ciudades) {
                        if (selectedIds.contains(data.nombre)) {
                            //stringBuilder.append("\n").append(data.nombre)
                            ciudadesSelected.add(Ciudades(data.nombre))
                        }
                    }

                    //Toast.makeText(context, ciudadesSelected[0].nombre + " noC", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(context, ciudadesSelected[1].nombre, Toast.LENGTH_SHORT).show()

                    findNavController().navigate(FavoritosFragmentDirections.actionFavoritosFragmentToCompararFragment(ciudadesSelected[0].nombre, ciudadesSelected[1].nombre))
                    mode.finish()

                    //Toast.makeText(context, "Selected items are :$stringBuilder", Toast.LENGTH_SHORT).show()
                    return true
                } else {
                    Toast.makeText(context, "Solo 2 ciudades", Toast.LENGTH_SHORT).show()
                    return false
                }

            }
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
        isMultiSelect = false
        selectedIds = ArrayList()
        adapter!!.setSelectedIds(ArrayList())
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritosFragment().apply {}
    }


}