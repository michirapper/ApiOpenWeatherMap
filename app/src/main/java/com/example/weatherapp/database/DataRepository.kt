package com.example.weatherapp.database

import android.content.Context
import kotlinx.coroutines.*

class DataRepository(context: Context) {
    private val usuarioDao: UsuarioDao? = AppDatabase.getInstance(context)?.usuarioDao()
    private val ciudadesDao: CiudadesDao? = AppDatabase.getInstance(context)?.ciudadesDao()
    private val usuariosCiudadesDao: UsuariosCiudadesDao? = AppDatabase.getInstance(context)?.usuariosCiudadesDao()

    fun insert(usuario: Usuario):Int {
        if (usuarioDao != null){
            CoroutineScope(Dispatchers.IO).launch {
                usuarioDao.insert(usuario)
            }
            return 0
        }
        return -1
    }
    fun insertCiudad(usuario: String, ciudades: Ciudades):Int {
        if (usuariosCiudadesDao != null && ciudadesDao!= null){
            CoroutineScope(Dispatchers.IO).launch {
                ciudadesDao.insert(ciudades)
                usuariosCiudadesDao.insert(UsuariosCiudadesCrossRef(usuario, ciudades.nombre))
            }
            return 0
        }
        return -1
    }

    fun isLogin(usuario: String, password:String): Boolean{

        var job : Job

        job = CoroutineScope(Dispatchers.IO).async {
            usuarioDao!!.countUsuarioByUsuarioPassword(usuario, password)!!
        }

        return runBlocking {
            job.await() == 1
        }
    }

    fun countUsuario():Int = runBlocking {
        usuarioDao!!.countUsuario()!!
    }

}