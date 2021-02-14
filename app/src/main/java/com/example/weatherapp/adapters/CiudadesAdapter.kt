package com.example.weatherapp.adapters

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.database.Ciudades
import com.example.weatherapp.fragments.FavoritosFragmentDirections
import com.example.weatherapp.model.CiudadesViewModel


class CiudadesAdapter(val context: Context?, var ciudades: List<Ciudades>, var ciudadesViewModel: CiudadesViewModel)
    : RecyclerView.Adapter<CiudadesAdapter.MyViewHolder>(), Filterable {

    private var selectedIds: List<String> = ArrayList()

    var ciudadesFilterList = ArrayList<Ciudades>()

    init{
        ciudadesFilterList = ciudades as ArrayList<Ciudades>
    }

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CiudadesAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ciudad_item, parent, false)
        val viewHolder = MyViewHolder(v)
        return viewHolder
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = ciudades[position].nombre
        val id = ciudades[position].nombre
        if (selectedIds.contains(id)){
            holder.rootView.foreground =
                context?.let { ContextCompat.getColor(it, R.color.colorControlActivated) }?.let {
                    ColorDrawable(
                        it
                    )
                }
        }else{
            holder.rootView.foreground =
                context?.let { ContextCompat.getColor(it, android.R.color.transparent) }?.let {
                    ColorDrawable(
                        it
                    )
                }
        }
        holder.itemView.setOnClickListener{
            Toast.makeText(context, holder.title.text, Toast.LENGTH_SHORT).show()
        }


//        holder.bindItems(ciudadesFilterList[position])
//        holder.itemView.setOnClickListener {
//
//            ciudadesViewModel.setCiudadSeleccionada(ciudadesFilterList[position])
//            //Toast.makeText(it.context, ciudadesFilterList[position].toString(), Toast.LENGTH_SHORT).show()
//
//            val bundle = bundleOf(Pair("ciudad", ciudadesFilterList[position].nombre))
//            val action = FavoritosFragmentDirections.actionFavoritosFragmentToMainWeather(bundle)
//            it.findNavController().navigate(action)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return ciudadesFilterList.size
    }

    fun getItem(position: Int): Ciudades {
        return ciudades[position]
    }

    fun setSelectedIds(selectedIds: List<String>) {
        this.selectedIds = selectedIds
        notifyDataSetChanged()
    }

    //the class is hodling the list view
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var rootView: LinearLayout

        init {
            title = itemView.findViewById(R.id.textViewTitulo)
            rootView = itemView.findViewById(R.id.root_view)
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