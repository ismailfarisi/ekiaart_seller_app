package com.example.ekiaartseller.domain

import android.location.Location
import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.io.Serializable


@Parcelize
data class OrderDetails(
    val orderId: String? = null,
    val userId: String? = null,
    val timestamp: Timestamp? = null,
    val products: List<Products>? = null,
    val location: Location? = null,
    val orderStatus : Int = 0
): Parcelable
@Parcelize
data class Product(
    val price : Int = 0,
    val priceString :String? = null,
    val productId : String? = null,
    val productName : String? = null
): Parcelable
@Parcelize
data class Products(
    val product : Product? = null,
    val quantity : Int = 0
): Parcelable