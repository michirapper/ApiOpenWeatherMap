package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.database.DataRepository
import com.example.weatherapp.utils.PasswordSecure

class LoginFragment : Fragment() {

    lateinit var editTextUsuario: EditText
    lateinit var editTextPassword: EditText
    lateinit var contrasenaSegura: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        editTextUsuario = v.findViewById<EditText>(R.id.editTextUsuario)
        editTextPassword = v.findViewById<EditText>(R.id.editTextPassword)
        val buttonLoginOk = v.findViewById<Button>(R.id.buttonLoginOk)

        buttonLoginOk.setOnClickListener{
            procesarLogin()
        }

        return v
    }


    private fun procesarLogin(){
        val dataRepository = DataRepository(requireContext())
        var passwordSecure = PasswordSecure
        contrasenaSegura = passwordSecure.sha256(editTextPassword.text.toString())
        if (dataRepository.countUsuario()==0) {
            //val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        else{
            if (dataRepository.isLogin(editTextUsuario.text.toString(), contrasenaSegura)){
                //val action = LoginFragmentDirections.actionLoginFragmentToListFragment()

                    //Aqui vamos a la aplicacion del tiempo

              //  findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

                Toast.makeText(requireContext(), "Datos correctos", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {}
    }
}