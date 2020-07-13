package com.example.ekiaartseller.ui.mainView

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ekiaartseller.data.ProductDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ShopFragmentVieModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val docRef = firestore.collection("productdetails")


    fun getProducts(): LiveData<List<ProductDetails>>{
        Log.d(TAG, "getProducts: called")
        val products = MutableLiveData<List<ProductDetails>>()
        docRef.get().addOnSuccessListener {
                val value  = mutableListOf<ProductDetails>()
                for (document in it){

                    value.add(document.toObject())
                    Log.d(TAG, "getProducts: $value")
                }
                products.postValue(value)
            }
        
        return products
    }
}