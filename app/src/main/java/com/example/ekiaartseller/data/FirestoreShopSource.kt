package com.example.ekiaartseller.data

import android.util.Log
import com.example.ekiaartseller.domain.OrderDetails
import com.example.ekiaartseller.domain.ProductDetails
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.domain.ShopDetails
import com.example.ekiaartseller.util.ORDER_DETAILS
import com.example.ekiaartseller.util.PRODUCT_DETAILS
import com.example.ekiaartseller.util.SHOP_DETAILS
import com.example.ekiaartseller.util.TAG
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.Exception


@ExperimentalCoroutinesApi
class FirestoreShopSource : ShopRepository {

    companion object{
        private var orderLastVisibleDocumentReached : DocumentSnapshot? = null
        private var orderLastDocumentReached = false
    }

    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!
    private val firestore = FirebaseFirestore.getInstance()
    private val shopRef = firestore.collection(SHOP_DETAILS)
    private val productRef = firestore.collection(SHOP_DETAILS).document(uid).collection(PRODUCT_DETAILS)
    private val orderRef = firestore.collection(SHOP_DETAILS).document(uid).collection(ORDER_DETAILS)


    @ExperimentalCoroutinesApi
    override suspend fun getShopDetails() :Flow<Result<ShopDetails>> = callbackFlow {
        offer(Result.Loading)
        shopRef.document(uid).get().addOnSuccessListener { documentSnapshot ->
            try{
                val data : ShopDetails = documentSnapshot.toObject()!!
                offer(Result.Success(data))
            }catch (e : Exception){
                offer(Result.Error(e))
            }
        }.addOnFailureListener {exception ->
            offer(Result.Error(exception))
        }
        awaitClose()
    }

    override suspend fun updateShopDetails() {
        shopRef.document(uid)
    }

    override suspend fun updateShopStatus(boolean: Boolean):Flow<Result<Unit>> = callbackFlow{
        offer(Result.Loading)
        val status = hashMapOf("status" to boolean)
        Log.d(TAG, "updateShopStatus: $status")
        shopRef.document(uid).set(status, SetOptions.merge()).addOnSuccessListener {
            offer(Result.Success(Unit))
        }.addOnFailureListener{
            offer(Result.Error(it))
        }
        awaitClose()
    }


    override suspend fun addProduct(productDetails: ProductDetails) :Flow<Result<Unit>> = callbackFlow {
        offer(Result.Loading)
        val productId = productRef.document().id
        productDetails.productId = productId
        productRef.add(productDetails).addOnSuccessListener {
            offer(Result.Success(Unit))
        }.addOnFailureListener {
            offer((Result.Error(it)))
        }
        awaitClose()
    }


    override suspend fun getProduct():Flow<Result<List<ProductDetails>>> = callbackFlow {
        offer(Result.Loading)
        productRef.addSnapshotListener { value, error ->
            try {
                val mutableList: MutableList<ProductDetails> = mutableListOf()
                for (product in value!!) {
                    val nproduct = product.toObject(ProductDetails::class.java)
                    Log.d(TAG, "getProduct: $nproduct")
                    mutableList.add(nproduct)
                }
                offer(Result.Success(mutableList))
            }catch (e :Exception){
                offer(Result.Error(e))
            }
            if (error != null)offer(Result.Error(error))
        }
        awaitClose()
    }

    override suspend fun updateProduct(productDetails: ProductDetails) {
        val productId = productDetails.productId!!
        productRef.document(productId).set(productDetails, SetOptions.mergeFields()).addOnSuccessListener {
            
        }.addOnFailureListener { 
            
        }
    }

    override suspend fun deleteProduct() {
        productRef.document().delete().addOnSuccessListener {

        }.addOnFailureListener {

        }
    }



    override suspend fun getNewOrder() = callbackFlow<Result<OrderDetails>> {
        val time = Timestamp.now()
        orderRef.whereGreaterThanOrEqualTo("timestamp",time).addSnapshotListener{ s, e ->
            for (dt in s?.documents!!){
                val obj = dt.toObject(OrderDetails::class.java)
                Log.d(TAG, "getNewOrder: ${obj}")
                offer(Result.Success(obj!!))
            }
        }

        awaitClose()
        }

    override suspend fun cancelNeworder() {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(): Flow<Result<List<OrderDetails>>> = callbackFlow {
        if (orderLastVisibleDocumentReached == null) {
            orderRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(10)
                .addSnapshotListener { s, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    val orderList: MutableList<OrderDetails> = mutableListOf()
                    for (doc in s!!) {
                        orderList.add(doc.toObject())
                    }
                    offer(Result.Success(orderList))
                    val snapshotSize = s.size()
                    if (snapshotSize < 10) {
                        orderLastDocumentReached = true
                    }
                    orderLastVisibleDocumentReached = s.documents.get(snapshotSize - 1)

                }
            awaitClose()
            return@callbackFlow
        }else if (orderLastDocumentReached == true){
            Log.d(TAG, "getOrders: last document reached")
            awaitClose()
            return@callbackFlow
        }else{
            orderRef.orderBy("timestamp", Query.Direction.DESCENDING).startAfter(
                orderLastVisibleDocumentReached!!).limit(7)
                .get().addOnSuccessListener {  s ->
                    val orderList: MutableList<OrderDetails> = mutableListOf()
                    for (doc in s!!) {
                        orderList.add(doc.toObject())
                    }
                    offer(Result.Success(orderList))
                    val snapshotSize = s.size()
                    if (snapshotSize < 7) {
                        orderLastDocumentReached = true
                    }
                    orderLastVisibleDocumentReached = s.documents.get(snapshotSize - 1)
                }
            awaitClose()
        }
        
    }



    override suspend fun checkShopStatus(): Flow<Result<Boolean>> = callbackFlow {
        shopRef.document(uid).get().addOnSuccessListener {
            val value = it.data?.get("status")
            if (value != null) {
                val boolean: Boolean = value as Boolean
                Log.d(TAG, "checkShopStatus: $boolean")
                offer(Result.Success(boolean))
            }
        }.addOnFailureListener {
            offer(Result.Error(it))
        }
        awaitClose()
    }
    override fun sendFCMRegistrationToServer(token:String?) {
        if (token != null) {
            val data = hashMapOf("fcmToken" to token)
            firestore.collection(SHOP_DETAILS).document(uid)
                .set(data, SetOptions.merge())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, "sendRegistrationToServer: fails")
                    } else {
                        Log.d(TAG, "sendRegistrationToServer: success")
                    }
                }
        }
    }

}