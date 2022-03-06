package com.example.ekiaartseller.ui.mainView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.ekiaartseller.R
import com.example.ekiaartseller.databinding.FragmentSettingsBinding
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.util.LoadingDialog
import com.example.ekiaartseller.util.TAG
import com.example.ekiaartseller.util.startLoginActivity
import com.google.firebase.auth.FirebaseAuth


class ShopSettingsFragment : Fragment(){

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: ShopViewModel
    private val auth :FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var loadingDialog : LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        getShopDetails()

        binding.shopImage.setOnClickListener {

        }
        binding.logoutBtn.setOnClickListener {
            signOut()
        }



        return binding.root
    }

    private fun getShopDetails() {
        viewModel.getShopDetails.observe(viewLifecycleOwner, Observer { result ->
            when (result){
                is Result.Success -> {
                    showToast("successful",null)
                    binding.shopNameTxtview.text = result.data.shopName
                }
                is Result.Loading -> {showToast("loading",null)}
                is Result.Error   -> {showToast("failed",result.exception)}
            }
        })
    }

    private fun showToast(s: String,e :Exception?) {
        Toast.makeText(this.context, s,Toast.LENGTH_SHORT).show()
        if (e != null){
            Log.e(TAG, "showToast:product add failed ", e)
        }
    }


    private fun signOut() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoadingDialog()
        auth.signOut()
        loadingDialog.stopLoadingDialog()
        requireActivity().startLoginActivity()
    }



}