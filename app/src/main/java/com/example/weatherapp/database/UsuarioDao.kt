package com.example.weatherapp.database

import androidx.room.*

@Dao
interface UsuarioDao {
    @Query("SELECT count(*) FROM usuario where usuario = :usuario and password = :password")
    suspend fun countUsuarioByUsuarioPassword(usuario: String, password: String): Int?

    @Query("SELECT count(*) FROM usuario")
    suspend fun countUsuario(): Int?

    @Query("SELECT count(*) FROM Ciudades")
    suspend fun countCiudades(): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario)

    @Transaction
    @Query("SELECT * FROM usuario WHERE usuario = :usuario")
    suspend fun getCiudades(usuario: String): List<UsuarioWithCiudades>

    @Transaction
    @Query("SELECT count(*) FROM UsuarioCiudadesCrossRef where usuario = :usuario and nombre = :ciudad")
    suspend fun estaEnFavoritos(usuario: String, ciudad: String): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCiudad(ciudades: Ciudades)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarioCiudadesCrossRef(crossRef: UsuarioCiudadesCrossRef)

    @Query("DELETE FROM UsuarioCiudadesCrossRef WHERE usuario = :usuario AND nombre = :ciudad")
    suspend fun borrarCiudad(usuario: String, ciudad: String)


}