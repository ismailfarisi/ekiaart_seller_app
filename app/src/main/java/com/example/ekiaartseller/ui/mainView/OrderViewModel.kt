package com.example.ekiaartseller.ui.mainView

import androidx.lifecycle.ViewModel
import com.example.ekiaartseller.data.FirestoreShopSource
import com.example.ekiaartseller.data.ShopRepository

class OrderViewModel: ViewModel() {
    private var repository: ShopRepository
    init {
        repository = FirestoreShopSource()
    }
    suspend fun order() = repository.getOrders()
}