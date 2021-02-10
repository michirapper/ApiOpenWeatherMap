package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CiudadesDao {

    @Query("SELECT count(*) FROM Ciudades")
    suspend fun countCiudades(): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ciudades: Ciudades)
}