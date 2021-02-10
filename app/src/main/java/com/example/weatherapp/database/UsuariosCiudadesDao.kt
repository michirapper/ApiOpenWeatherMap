package com.example.weatherapp.database

import androidx.room.*

@Dao
interface UsuariosCiudadesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(join: UsuariosCiudadesCrossRef)

    @Transaction
    @Query("SELECT * FROM usuario WHERE usuario = :usuario")
    suspend fun getCiudades(usuario: Int): Array<UsuariosCiudades>
}