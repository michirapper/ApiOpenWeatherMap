package com.example.weatherapp.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class UsuariosCiudades(
    @Embedded var usuario: Usuario,

    @Relation(
        parentColumn = "usuario",
        entity = Ciudades::class,
        entityColumn = "nombre",
        associateBy = Junction(value = UsuariosCiudadesCrossRef::class,
            parentColumn = "usuario",
            entityColumn = "nombre")
    )

    var ciudades: List<Ciudades>
)

@Entity(primaryKeys = ["usuario", "nombre"])
data class UsuariosCiudadesCrossRef(
    val usuario: String,
    val nombre: String
)