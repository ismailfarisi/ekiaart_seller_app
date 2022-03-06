package com.example.ekiaartseller.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ekiaartseller.databinding.OrderListItemBinding
import com.example.ekiaartseller.domain.OrderDetails
import com.google.android.material.animation.AnimationUtils

class OrderListAdapter : ListAdapter<OrderDetails,RecyclerView.ViewHolder>(OrderDiffCallBack()) {

    companion object{
        private  var currentPosition : Int? = null
    }
    fun addData(listItems: List<OrderDetails>) {
        val size = itemCount
        this.submitList(currentList + listItems)
        val sizeNew = itemCount
        notifyItemRangeChanged(size, sizeNew)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  OrderListViewHolder(
            OrderListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = getItem(position)
        (holder as OrderListViewHolder).bind(order)
        holder.expandableLayout.visibility = View.GONE
        if (currentPosition == position){
            holder.expandableLayout.visibility = View.VISIBLE
        }
        holder.cardView.setOnClickListener {
            if(currentPosition != position) {
                currentPosition = position
                notifyDataSetChanged()
            }else{
                currentPosition = null
                notifyDataSetChanged()
            }
        }

    }
    class OrderListViewHolder(private val binding: OrderListItemBinding) : RecyclerView.ViewHolder(binding.root){
        val expandableLayout = binding.expandableLayout
        val cardView = binding.orderItemCardview
        /*var visible = false
        init {
            binding.orderItemCardview.setOnClickListener {
                if (visible == false){
                    binding.expandableLayout.visibility = View.VISIBLE
                    visible = true
                }else{
                    binding.expandableLayout.visibility = View.GONE
                    visible = false
                }

            }
        }*/
        fun bind(order : OrderDetails){
            val orderId = "OrderId ${order.orderId}"
            binding.orderNo.text = orderId
            val adapter =OrderProductListAdapter()
            binding.orderProductListRecyclerview.also {
                it.adapter = adapter
                it.setHasFixedSize(true)
            }
            adapter.submitList(order.products)
        }


    }
}

class OrderDiffCallBack : DiffUtil.ItemCallback<OrderDetails>(){
    override fun areItemsTheSame(oldItem: OrderDetails, newItem: OrderDetails): Boolean {
        return oldItem.orderId == newItem.orderId
    }

    override fun areContentsTheSame(oldItem: OrderDetails, newItem: OrderDetails): Boolean {
        return oldItem == newItem
    }

}
