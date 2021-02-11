package com.example.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.database.Ciudades
import com.example.weatherapp.fragments.FavoritosFragmentDirections
import com.example.weatherapp.model.CiudadesViewModel
import kotlin.coroutines.coroutineContext


class CiudadesAdapter(var ciudades: List<Ciudades>, var ciudadesViewModel: CiudadesViewModel)
    : RecyclerView.Adapter<CiudadesAdapter.ViewHolder>(), Filterable {

    var ciudadesFilterList = ArrayList<Ciudades>()

    init{
        ciudadesFilterList = ciudades as ArrayList<Ciudades>
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CiudadesAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ciudad_item, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CiudadesAdapter.ViewHolder, position: Int) {
        holder.bindItems(ciudadesFilterList[position])
        holder.itemView.setOnClickListener {

            ciudadesViewModel.setCiudadSeleccionada(ciudadesFilterList[position])
            //Toast.makeText(it.context, ciudadesFilterList[position].toString(), Toast.LENGTH_SHORT).show()

            val bundle = bundleOf(Pair("ciudad", ciudadesFilterList[position].nombre))
            val action = FavoritosFragmentDirections.actionFavoritosFragmentToMainWeather(bundle)
            it.findNavController().navigate(action)

        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return ciudadesFilterList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(ciudades: Ciudades) {
            val textViewNombre = itemView.findViewById<TextView>(R.id.textViewTitulo)
            textViewNombre.text = ciudades.nombre
        }
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSequence = constraint.toString()
                if (charSequence.isEmpty()){
                    ciudadesFilterList = ciudades as ArrayList<Ciudades>
                }
                else{
                    val resultList = ArrayList<Ciudades>()
                    for (row in ciudades){
                        if (row.nombre?.toLowerCase()?.contains(charSequence.toLowerCase())!!){
                            resultList.add(row)
                        }
                    }
                    ciudadesFilterList = resultList
                }
                var filterResult = FilterResults()
                filterResult.values = ciudadesFilterList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ciudadesFilterList = results?.values as ArrayList<Ciudades>
                notifyDataSetChanged()
            }
        }
    }
}