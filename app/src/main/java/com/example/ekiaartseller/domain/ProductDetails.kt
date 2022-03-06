package com.example.ekiaartseller.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDetails (
    var productId : String? = null,
    val productName : String? = null,
    val price : Double? = null,
    val avgRating : Float? = null,
    val productDescription :String? = null,
    val available : Boolean? = true,
    val subCategory: String? = null,
    val unit : String? = null
):Parcelable

