package com.example.ekiaartseller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ekiaartseller.databinding.ProductListOrderItemBinding
import com.example.ekiaartseller.domain.OrderProductDetails
import com.example.ekiaartseller.domain.Products


class OrderProductListAdapter : ListAdapter<Products,RecyclerView.ViewHolder>(OrderProductListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderProductViewHolder(
            ProductListOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as OrderProductViewHolder).bind(product)
    }
    inner class OrderProductViewHolder(private  val binding : ProductListOrderItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(producte : Products){
            val qty = producte.quantity.toString()+"x"
            binding.productName.text = producte.product?.productName
            binding.productQty.text = qty
        }
    }
}

class OrderProductListDiffCallback : DiffUtil.ItemCallback<Products>(){
    override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
        return oldItem.product == newItem.product
    }

    override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
        return oldItem == newItem
    }

}
