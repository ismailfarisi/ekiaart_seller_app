package com.example.ekiaartseller.adapter



import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ekiaartseller.domain.ProductDetails
import com.example.ekiaartseller.databinding.ProductListItemBinding
import com.example.ekiaartseller.util.TAG
import com.google.firebase.firestore.FirebaseFirestore

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
        private lateinit var productId : String
        init {
            binding.switch1.setOnClickListener {
                val firestore = FirebaseFirestore.getInstance()
                if (binding.switch1.isChecked){
                    Log.d(TAG, "ggggggg$productId: ")
                    firestore.collection("productdetails").document(productId).update("available",true)
                }else{
                    firestore.collection("productdetails").document(productId).update("available",false)
                }
            }
        }
        fun bind(item : ProductDetails){
            productId = item.productId!!
            binding.productName.text = item.productName
            binding.productDescription.text = item.productDescription
            binding.ratingBar.rating = item.avgRating ?:3.5F
            binding.switch1.isChecked = item.available?:true
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
