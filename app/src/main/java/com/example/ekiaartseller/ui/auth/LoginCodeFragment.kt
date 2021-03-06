package com.example.ekiaartseller.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ekiaartseller.R
import com.example.ekiaartseller.data.FirebaseSource
import com.example.ekiaartseller.data.UserRepository
import com.example.ekiaartseller.databinding.FragmentLoginCodeBinding
import com.example.ekiaartseller.ui.interface1.IAuth
import com.example.ekiaartseller.util.LoadingDialog
import com.example.ekiaartseller.util.TAG
import com.example.ekiaartseller.util.startMainActivity


class LoginCodeFragment : Fragment(), IAuth {

    private lateinit var viewModel: LoginFragmentViewModel
    private lateinit var binding: FragmentLoginCodeBinding
    private lateinit var loadingDialog: LoadingDialog
    private val factory by lazy {
        LoginFragmentViewModelFactory(UserRepository(FirebaseSource()),null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity(),factory).get(LoginFragmentViewModel::class.java)
        binding = FragmentLoginCodeBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(requireActivity())
        viewModel.iAuth = this
        binding.codeBtn.setOnClickListener {
            onCodeEntered() }


        return binding.root
    }

    private fun onCodeEntered() {
        loadingDialog.startLoadingDialog()
        val code = "121212"+binding.codeInput.text.toString()
        viewModel.verifyCode(code)
    }

    override fun loginSuccess() {
        loadingDialog.stopLoadingDialog()
        Log.d(TAG, "loginSuccess: ")
        requireActivity().startMainActivity()
    }

    override fun newUserRegister() {
        loadingDialog.stopLoadingDialog()
        Navigation.findNavController(binding.codeBtn).navigate(R.id.nav_to_reg)
    }

    override fun loginFailed() {
        loadingDialog.stopLoadingDialog()
    }


}