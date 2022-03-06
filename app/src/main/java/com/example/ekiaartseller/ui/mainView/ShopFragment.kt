package com.example.ekiaartseller.ui.mainView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekiaartseller.R
import com.example.ekiaartseller.adapter.ProductListAdapter
import com.example.ekiaartseller.databinding.FragmentShopBinding
import com.example.ekiaartseller.domain.ProductDetails
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.ui.mainView.dialogfragment.AddProductDialogFragment
import com.example.ekiaartseller.util.LoadingDialog
import com.example.ekiaartseller.util.TAG
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class ShopFragment : Fragment() ,AddProductDialogFragment.NoticeDialogListener{

    private lateinit var binding: FragmentShopBinding
    private lateinit var vieModel: ShopViewModel
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater,container,false)
        vieModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        loadingDialog = LoadingDialog(this.requireContext())

        val adapter = ProductListAdapter()
        binding.productListRecyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this.context)
        }
        subscribeUI(adapter)

        binding.floatingActionButton.setOnClickListener {
            onAddProductPressed()
        }




        return binding.root
    }

    private fun subscribeUI(adapter: ProductListAdapter) {
        Log.d(TAG, "subscribeUI: observing")
        lifecycleScope.launchWhenCreated {
            vieModel.getProducts().collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "subscribeUI: success")
                        loadingDialog.stopLoadingDialog()
                        adapter.submitList(result.data)
                    }
                    is Result.Error -> {
                        loadingDialog.stopLoadingDialog()
                        showErrorToast(result.exception)
                    }
                    is Result.Loading -> loadingDialog.startLoadingDialog()
                }
            }
        }
    }

    private fun showErrorToast(exception: Exception) {
        Log.e(TAG, "showErrorToast: ",exception )
        Toast.makeText(this.context,"Error Loading Products", Toast.LENGTH_SHORT).show()
    }

    private fun onAddProductPressed() {
        val dialog = AddProductDialogFragment()
        dialog.setTargetFragment(this,1)
        dialog.show(parentFragmentManager,"ADD_PRODUCT")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, productDetails: ProductDetails) {
        lifecycleScope.launchWhenCreated {
            vieModel.addProduct(productDetails).collect{ result ->
                when (result) {
                    is Result.Success -> dialog.dismiss()
                }
            }
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        dialog.dismiss()
    }

}