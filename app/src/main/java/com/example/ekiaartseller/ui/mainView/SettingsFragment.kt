package com.example.ekiaartseller.mainView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ekiaartseller.R
import com.example.ekiaartseller.databinding.FragmentSettingsBinding
import com.example.ekiaartseller.util.LoadingDialog
import com.example.ekiaartseller.util.startLoginActivity
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment(){

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: MainViewModel
    private val auth :FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var loadingDialog : LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        binding.signOutBtn.setOnClickListener {
            signOut()
        }
        binding.addProduct.setOnClickListener {
            onAddProductPressed(it)
        }

        return binding.root
    }

    private fun onAddProductPressed(view :View) {
        Navigation.findNavController(view).navigate(R.id.nav_to_addProductDialog)
    }

    private fun signOut() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoadingDialog()
        auth.signOut()

        requireActivity().startLoginActivity()
    }



}