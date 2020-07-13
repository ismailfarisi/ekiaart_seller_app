package com.example.ekiaartseller.data

data class ProductDetails (
    val productId : String = "",
    val shopId : String = "",
    val productName : String = "",
    val price : Double = 0.0,
    val avgRating : Double = 3.5,
    val productDescription :String = "",
    val available : Boolean = true,
    val subCategory: String = ""

)