package com.example.ekiaartseller.mainView

import androidx.lifecycle.ViewModel
import com.example.ekiaartseller.data.ProductDetails
import com.example.ekiaartseller.mainView.`interface`.INewProduct
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {

    val productFirestore :FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var iNewProduct : INewProduct

    fun addProduct(data : ProductDetails){
        productFirestore.collection("productdetails").document().set(data).addOnSuccessListener {
            iNewProduct.productAddedSuccessfully()
        }.addOnFailureListener{
            iNewProduct.productAddingFailed()
        }
    }
}