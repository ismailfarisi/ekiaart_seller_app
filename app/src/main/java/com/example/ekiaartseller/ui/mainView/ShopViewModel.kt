package com.example.ekiaartseller.ui.mainView

import androidx.lifecycle.*
import com.example.ekiaartseller.data.FirestoreShopSource
import com.example.ekiaartseller.data.ShopRepository
import com.example.ekiaartseller.domain.ProductDetails
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.domain.ShopDetails
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShopViewModel: ViewModel() {

    private val shopRepository :ShopRepository
    init {
        shopRepository = FirestoreShopSource()
    }

    val getShopDetails : LiveData<Result<ShopDetails>> = liveData(viewModelScope.coroutineContext) {
        emitSource(shopRepository.getShopDetails().asLiveData())
    }

    suspend fun getProducts() = shopRepository.getProduct()

    suspend fun addProduct (data : ProductDetails)= shopRepository.addProduct(data)




}

