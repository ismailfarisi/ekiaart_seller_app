package com.example.ekiaartseller.ui.mainView.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.ekiaartseller.databinding.FragmentAddProductDialogBinding
import com.example.ekiaartseller.domain.ProductDetails
import com.example.ekiaartseller.ui.mainView.ShopViewModel


class AddProductDialogFragment : DialogFragment(){

    private lateinit var binding: FragmentAddProductDialogBinding
    private lateinit var viewModel: ShopViewModel
    private lateinit var listener : NoticeDialogListener



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductDialogBinding.inflate(inflater,container,false)
        val view = binding.root
        listener = targetFragment as NoticeDialogListener

        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)


        binding.productAddBtn.setOnClickListener { onAddBtnPressed() }
        binding.productCanceladdingBtn.setOnClickListener {
            listener.onDialogNegativeClick(this)
        }



        return view
    }

    private fun onAddBtnPressed() {

        val productName = binding.productNameInput.text.toString()
        val productPrice = binding.productPriceInput.text.toString().toDouble()
        val productSubcategory = binding.productSubCategoryInput.text.toString()
        val productDescription = binding.productDescriptionInput.text.toString()

        val productDetails = ProductDetails(
            productName = productName,
            price = productPrice,
            subCategory = productSubcategory,
            productDescription = productDescription
        )
        listener.onDialogPositiveClick(this,productDetails)

    }
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, productDetails: ProductDetails)
        fun onDialogNegativeClick(dialog: DialogFragment)
        }
}