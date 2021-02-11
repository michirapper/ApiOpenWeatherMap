package com.example.weatherapp.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class UsuarioWithCiudades(
    @Embedded val usuario: Usuario,
    @Relation(
        parentColumn = "usuario",
        entityColumn = "nombre",
        associateBy = Junction(UsuarioCiudadesCrossRef::class)
    )
    val ciudades: List<Ciudades>
)

@Entity(primaryKeys = ["usuario", "nombre"])
data class UsuarioCiudadesCrossRef(
    val usuario: String,
    val nombre: String
)