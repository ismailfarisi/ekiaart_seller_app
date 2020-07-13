package com.example.ekiaartseller.ui.auth

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ekiaartseller.R
import com.example.ekiaartseller.databinding.FragmentLoginBinding
import com.example.ekiaartseller.util.LoadingDialog


class LoginFragment: Fragment(), IAuthLogin {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginFragmentViewModel
    private lateinit var loadingDialog: LoadingDialog


    private lateinit var phno :String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(requireActivity())
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity()).get(LoginFragmentViewModel::class.java)
        viewModel.iAuthLogin = this
        binding.button.setOnClickListener {
            onLoginPressed()}
        return view
    }





    fun onLoginPressed() {

        loadingDialog.startLoadingDialog()
        phno = "+919898989898" + binding.phnInp.text.toString()
        Log.d(TAG, "onLoginPressed: ")
        viewModel.login(phno, requireActivity())


    }

    override fun onCodeSent() {
        loadingDialog.stopLoadingDialog()
        Navigation.findNavController(requireView()).navigate((R.id.nav_to_code))
    }

    override fun verificationFailed() {
        loadingDialog.stopLoadingDialog()
       Toast.makeText(requireContext(),"SOMETHING WENT WRONG",Toast.LENGTH_SHORT).show()
    }


}