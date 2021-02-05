package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT count(*) FROM usuario where usuario = :usuario and password = :password")
    suspend fun countUsuarioByUsuarioPassword(usuario:String, password: String): Int?

    @Query("SELECT count(*) FROM usuario")
    suspend fun countUsuario(): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuario: Usuario)
}