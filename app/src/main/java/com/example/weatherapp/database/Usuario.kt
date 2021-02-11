package com.example.weatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario (
    @PrimaryKey(autoGenerate = false)
    val usuario: String,
    val password: String,
    val nombre: String
)