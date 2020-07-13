package com.example.ekiaartseller.ui.mainView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ekiaartseller.R
import com.example.ekiaartseller.adapter.ProductListAdapter
import com.example.ekiaartseller.data.ProductDetails
import com.example.ekiaartseller.databinding.FragmentShopBinding


class ShopFragment : Fragment() {

    private lateinit var binding: FragmentShopBinding
    private lateinit var vieModel: ShopFragmentVieModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater,container,false)
        vieModel = ViewModelProvider(this).get(ShopFragmentVieModel::class.java)

        val adapter = ProductListAdapter()
        binding.productListRecyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this.context)
        }
        subscribeUI(adapter)




        return binding.root
    }

    private fun subscribeUI(adapter: ProductListAdapter) {
        vieModel.getProducts().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}