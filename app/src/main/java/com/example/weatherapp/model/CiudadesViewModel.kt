package com.example.weatherapp.model

import androidx.lifecycle.ViewModel
import com.example.weatherapp.database.Ciudades

class CiudadesViewModel: ViewModel() {
    private var ciudadSelecionada: Ciudades

    init{
        ciudadSelecionada = Ciudades("")
    }

    fun getCiudadSeleccionada():Ciudades{
        return ciudadSelecionada
    }

    fun setCiudadSeleccionada(ciudades: Ciudades){
        ciudadSelecionada = ciudades
    }

}