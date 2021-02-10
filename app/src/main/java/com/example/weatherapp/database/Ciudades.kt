package com.example.weatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ciudades(
    @PrimaryKey val nombre: String
)