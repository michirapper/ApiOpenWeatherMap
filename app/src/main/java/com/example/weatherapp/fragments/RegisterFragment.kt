package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.database.DataRepository
import com.example.weatherapp.database.Usuario
import com.example.weatherapp.utils.PasswordSecure


class RegisterFragment : Fragment() {
    lateinit var editTextUsuario: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextNombre: EditText
    lateinit var contrasenaSegura: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var v = inflater.inflate(R.layout.fragment_register, container, false)

        editTextUsuario = v.findViewById<EditText>(R.id.editTextRegisterUsuario)
        editTextPassword = v.findViewById<EditText>(R.id.editTextRegisterPassword)
        editTextNombre = v.findViewById<EditText>(R.id.editTextRegisterNombre)
        val buttonRegisterOk = v.findViewById<Button>(R.id.buttonRegisterOk)

        buttonRegisterOk.setOnClickListener{
            procesarRegister()
        }
        return v
    }


    private fun procesarRegister(){
        val dataRepository = DataRepository(requireContext())
        var passwordSecure = PasswordSecure
        contrasenaSegura = passwordSecure.sha256(editTextPassword.text.toString())
        if (dataRepository.insert(Usuario(editTextUsuario.text.toString(), contrasenaSegura, editTextNombre.text.toString())) == -1) {
            Toast.makeText(requireContext(), "Usuario repetido", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply { }
    }

}