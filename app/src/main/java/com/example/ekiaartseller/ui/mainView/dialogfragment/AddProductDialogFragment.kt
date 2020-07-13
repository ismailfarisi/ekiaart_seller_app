package com.example.ekiaartseller.mainView.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.ekiaartseller.data.ProductDetails
import com.example.ekiaartseller.databinding.FragmentAddProductDialogBinding
import com.example.ekiaartseller.mainView.MainViewModel
import com.example.ekiaartseller.mainView.`interface`.INewProduct
import com.example.ekiaartseller.util.LoadingDialog


class AddProductDialogFragment : DialogFragment(),
    INewProduct {

    private lateinit var binding: FragmentAddProductDialogBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var loadingDialog: LoadingDialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductDialogBinding.inflate(inflater,container,false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.iNewProduct = this
        loadingDialog = LoadingDialog(requireActivity())

        binding.productAddBtn.setOnClickListener { onAddBtnPressed() }
        binding.productCanceladdingBtn.setOnClickListener {
            dialog?.cancel()
        }



        return view
    }

    private fun onAddBtnPressed() {
        loadingDialog.startLoadingDialog()
        val productName = binding.productNameInput.toString()
        val productPrice = binding.productPriceInput.toString()
        val productSubcategory = binding.productSubCategoryInput.toString()
        val productDescription = binding.productDescriptionInput.toString()

        val productDetails = ProductDetails(productName = productName,
            price = productPrice,
            subCategory = productSubcategory,
            productDescription = productDescription)
        viewModel.addProduct(productDetails)

    }

    override fun productAddedSuccessfully() {
        Toast.makeText(requireContext(),"product added successfully",Toast.LENGTH_SHORT).show()
        loadingDialog.stopLoadingDialog()
        dialog?.cancel()

    }

    override fun productAddingFailed() {
        Toast.makeText(requireContext(),"product added failed",Toast.LENGTH_SHORT).show()
        loadingDialog.stopLoadingDialog()
        dialog?.cancel()
    }


}