package com.example.ekiaartseller.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ekiaartseller.R
import com.example.ekiaartseller.data.FirebaseSource
import com.example.ekiaartseller.domain.ShopDetails
import com.example.ekiaartseller.data.UserRepository
import com.example.ekiaartseller.databinding.FragmentRegisterBinding
import com.example.ekiaartseller.ui.interface1.IDataUpdated
import com.example.ekiaartseller.util.startMainActivity


class RegisterFragment : Fragment(),AdapterView.OnItemSelectedListener,
    IDataUpdated {

    lateinit var binding: FragmentRegisterBinding
    lateinit var viewModel: LoginFragmentViewModel
    lateinit var categoryName : String
    private val factory by lazy {
        LoginFragmentViewModelFactory(UserRepository(FirebaseSource()),null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this,factory).get(LoginFragmentViewModel::class.java)



        val spinner : Spinner = binding.spinner
        ArrayAdapter.createFromResource(this.requireContext(),
            R.array.category_array,
            android.R.layout.simple_spinner_item).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }
        spinner.onItemSelectedListener =this

        binding.registerButton.setOnClickListener {registerDetails()}


        return binding.root
    }

    private fun registerDetails() {
        val shopName = binding.shopNameInput.text.toString()
        val location = binding.locationInput.text.toString()
        val postcode = binding.postPinInput.text.toString()

        val registerData = ShopDetails(
            shopName,
            location,
            postcode,
            categoryName
        )
        viewModel.regiterData(registerData)


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
       categoryName = parent?.getItemAtPosition(pos).toString()
    }

    override fun onSuccess() {
        requireActivity().startMainActivity()
    }

    override fun onFailure() {
        TODO("Not yet implemented")
    }
}