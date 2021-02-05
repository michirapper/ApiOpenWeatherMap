package com.example.weatherapp.database

import android.content.Context
import kotlinx.coroutines.*

class DataRepository(context: Context) {
    private val usuarioDao: UsuarioDao? = AppDatabase.getInstance(context)?.usuarioDao()

    fun insert(usuario: Usuario):Int {
        if (usuarioDao != null){
            CoroutineScope(Dispatchers.IO).launch {
                usuarioDao.insert(usuario)
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