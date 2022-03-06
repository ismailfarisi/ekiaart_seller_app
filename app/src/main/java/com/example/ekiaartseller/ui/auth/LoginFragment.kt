package com.example.ekiaartseller.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ekiaartseller.R
import com.example.ekiaartseller.data.FirebaseSource
import com.example.ekiaartseller.data.UserRepository
import com.example.ekiaartseller.databinding.FragmentLoginBinding
import com.example.ekiaartseller.ui.interface1.IAuthLogin
import com.example.ekiaartseller.util.LoadingDialog
import com.example.ekiaartseller.util.TAG


class LoginFragment: Fragment(),
    IAuthLogin {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginFragmentViewModel
    private lateinit var loadingDialog: LoadingDialog
    private val factory by lazy {
        LoginFragmentViewModelFactory(UserRepository(FirebaseSource()),this)
    }



    private lateinit var phno :String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        loadingDialog = LoadingDialog(requireActivity())
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity(),factory).get(LoginFragmentViewModel::class.java)
        binding.button.setOnClickListener {
            onLoginPressed()}
        return view
    }





    private fun onLoginPressed() {

        loadingDialog.startLoadingDialog()
        phno = "+919898989898" + binding.phnInp.text.toString()
        Log.d(TAG, "onLoginPressed: ")
        viewModel.loginPhoneOtpRequest(phno, requireActivity())


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