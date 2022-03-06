package com.example.ekiaartseller.domain

data class ShopDetails(
    val shopName :String="",
    val location :String="",
    val postCode :String="",
    val category :String="",
    var shopId : String ="",
    val status : Boolean = false

)