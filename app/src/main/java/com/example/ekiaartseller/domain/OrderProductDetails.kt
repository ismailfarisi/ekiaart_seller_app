package com.example.ekiaartseller.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProductDetails(
    val productId : String? = null,
    val productName : String? = null,
    val quantity : Int? = 0
): Parcelable