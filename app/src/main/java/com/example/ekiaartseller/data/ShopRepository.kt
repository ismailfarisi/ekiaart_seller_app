package com.example.ekiaartseller.data


import com.example.ekiaartseller.domain.OrderDetails
import com.example.ekiaartseller.domain.ProductDetails
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.domain.ShopDetails
import kotlinx.coroutines.flow.Flow

interface ShopRepository {
    suspend fun getShopDetails():Flow<Result<ShopDetails>>
    suspend fun updateShopDetails()
    suspend fun updateShopStatus(boolean: Boolean):Flow<Result<Unit>>
    suspend fun addProduct(productDetails: ProductDetails): Flow<Result<Unit>>
    suspend fun getProduct():Flow<Result<List<ProductDetails>>>
    suspend fun updateProduct(productDetails: ProductDetails)
    suspend fun deleteProduct()
    suspend fun getNewOrder(): Flow<Result<OrderDetails>>
    suspend fun cancelNeworder()
    suspend fun getOrders() :Flow<Result<List<OrderDetails>>>
    suspend fun checkShopStatus():Flow<Result<Boolean>>
    fun sendFCMRegistrationToServer(token:String?)
}