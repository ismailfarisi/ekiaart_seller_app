package com.example.ekiaartseller.adapter


import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ekiaartseller.data.ProductDetails
import com.example.ekiaartseller.databinding.ProductListItemBinding

class ProductListAdapter: ListAdapter<ProductDetails, RecyclerView.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductListViewHolder(
            ProductListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as ProductListViewHolder).bind(product)

    }


    class ProductListViewHolder(private val binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.switch1.setOnClickListener {
                if (binding.switch1.isChecked){
                    Log.d(TAG, "switch: checked dddd")
                }
            }
        }
        fun bind(item :ProductDetails){
            binding.productName.text = item.productName
            binding.productDescription.text = item.productDescription
            binding.ratingBar.rating = item.avgRating.toFloat()
            binding.switch1.isActivated = item.available
        }


    }
}

private class ProductDiffCallback : DiffUtil.ItemCallback<ProductDetails>(){
    override fun areItemsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
        return newItem.productId == oldItem.productId
    }

    override fun areContentsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
        return oldItem == newItem
    }

}
