package com.example.ekiaartseller.ui.mainView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ekiaartseller.R
import com.example.ekiaartseller.ui.auth.LoginFragmentViewModel
import com.example.ekiaartseller.databinding.FragmentSplashBinding
import com.example.ekiaartseller.util.startMainActivity


class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    lateinit var viewModel: LoginFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.currentUser != null){
            requireActivity().startMainActivity()

        }else {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }


}
