package com.example.weatherapp.database

import android.content.Context
import kotlinx.coroutines.*

class DataRepository(context: Context) {
    private val usuarioDao: UsuarioDao? = AppDatabase.getInstance(context)?.usuarioDao()
    //private val ciudadesDao: CiudadesDao? = AppDatabase.getInstance(context)?.ciudadesDao()
    //private val usuariosCiudadesDao: UsuariosCiudadesDao? = AppDatabase.getInstance(context)?.usuariosCiudadesDao()

    fun insert(usuario: Usuario): Int {
        if (usuarioDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                usuarioDao.insert(usuario)
            }
            return 0
        }
        return -1
    }

    fun insertCiudad(ciudades: Ciudades, usuario: String ): Int {
       // if (usuariosCiudadesDao != null && ciudadesDao != null) {
        if (usuarioDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                usuarioDao.insertCiudad(ciudades)
                usuarioDao.insertUsuarioCiudadesCrossRef(UsuarioCiudadesCrossRef(usuario, ciudades.nombre))
            }
            return 0
        }
        return -1
    }

    fun getCiudades(usuario: String):List<UsuarioWithCiudades> = runBlocking {
        usuarioDao!!.getCiudades(usuario)
    }

    fun borrarCiudad(usuario: String, ciudad: String) = runBlocking {
        usuarioDao!!.borrarCiudad(usuario, ciudad)
    }

    fun isLogin(usuario: String, password: String): Boolean {

        var job: Job

        job = CoroutineScope(Dispatchers.IO).async {
            usuarioDao!!.countUsuarioByUsuarioPassword(usuario, password)!!
        }

        return runBlocking {
            job.await() == 1
        }
    }
    fun isFavoritos(usuario: String, ciudad: String): Boolean {

        var job: Job

        job = CoroutineScope(Dispatchers.IO).async {
            usuarioDao!!.estaEnFavoritos(usuario, ciudad)!!
        }

        return runBlocking {
            job.await() == 1
        }
    }

    fun countUsuario(): Int = runBlocking {
        usuarioDao!!.countUsuario()!!
    }

}