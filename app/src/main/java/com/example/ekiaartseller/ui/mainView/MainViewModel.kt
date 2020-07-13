package com.example.ekiaartseller.ui.mainView

import androidx.lifecycle.ViewModel
import com.example.ekiaartseller.data.ProductDetails
import com.example.ekiaartseller.ui.interface1.INewProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {

    private val productFirestore :FirebaseFirestore = FirebaseFirestore.getInstance()
    val shopid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var iNewProduct : INewProduct
    val productId = productFirestore.collection("productdetails").document().id

    fun addProduct(data : ProductDetails){
        productFirestore.collection("productdetails").document().set(data).addOnSuccessListener {
            iNewProduct.productAddedSuccessfully()
        }.addOnFailureListener{
            iNewProduct.productAddingFailed()
        }
    }
}