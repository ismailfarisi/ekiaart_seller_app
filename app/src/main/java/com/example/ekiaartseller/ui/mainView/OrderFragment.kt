package com.example.ekiaartseller.ui.mainView

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekiaartseller.R
import com.example.ekiaartseller.adapter.OrderListAdapter
import com.example.ekiaartseller.databinding.FragmentOrderBinding
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.util.TAG
import kotlinx.coroutines.flow.collect
import java.lang.Exception


class OrderFragment : Fragment() {

    private  lateinit var binding: FragmentOrderBinding
    private lateinit var viewModel : OrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        val adapter =  OrderListAdapter()
        binding.orderListRecyclerview.also {
            var isLastPage: Boolean = false
            var isLoading: Boolean = false
            it.adapter = adapter
            val layoutManager = it.layoutManager
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            loadMoreItems(adapter)
                        }//                    && totalItemCount >= ClothesFragment.itemsCount
                    }

                }
            })
        }
        subscribeUI(adapter)

        return  view
    }
    private fun loadMoreItems(adapter: OrderListAdapter){
        subscribeUI(adapter,false)
    }

    private fun subscribeUI(adapter: OrderListAdapter,calledFirst : Boolean = true) {
        Log.d(TAG, "order subscribeUI: observing")
        lifecycleScope.launchWhenCreated {
            viewModel.order().collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "subscribeUI: success ${result.data}")
                        if (calledFirst) {
                            adapter.submitList(result.data)
                        }else{
                            adapter.addData(result.data)
                        }
                    }
                    is Result.Error -> {

                        showErrorToast(result.exception)
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }
    private fun showErrorToast(exception: Exception) {
        Log.e(TAG, "showErrorToast: ",exception )
        Toast.makeText(this.context,"Error Loading Orders", Toast.LENGTH_SHORT).show()
    }

}