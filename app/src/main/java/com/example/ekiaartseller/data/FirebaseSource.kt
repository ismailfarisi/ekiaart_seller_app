package com.example.ekiaartseller.data

import android.util.Log
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.domain.ShopDetails
import com.example.ekiaartseller.ui.interface1.IDataUpdated
import com.example.ekiaartseller.util.SHOP_DETAILS
import com.example.ekiaartseller.util.TAG
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FirebaseSource {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }
    suspend fun cc(credential: PhoneAuthCredential){
        CoroutineScope(Dispatchers.IO).launch {
            val result1 = async {
                signInWithPhoneAuthCredential(credential)
            }.await()
            when (result1){
                is Result.Success -> {
                    if (result1.data){

                    }
                }
            }
        }
    }

    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) : Result<Boolean> = suspendCoroutine{ cont ->

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val uid = task.result?.user?.uid!!
                    firestore.collection(SHOP_DETAILS).document(uid).get().addOnCompleteListener() {
                        if (task.isSuccessful) {
                            cont.resume(
                                if (it.result?.data != null) {
                                    Log.d(TAG, "checkShopDetailExist: true")

                                    Result.Success(true)
                                } else {
                                    Log.d(TAG, "checkShopDetailExist: false")
                                    Result.Success(false)
                                }
                            )
                        }else{
                            cont.resume(Result.Error(task.exception!!))
                        }
                    }
                } else {
                    cont.resume(Result.Error(task.exception!!))
                }
            }
    }

    @ExperimentalCoroutinesApi
    suspend fun registerData(data: ShopDetails) : Flow<Result<Unit>> = callbackFlow {
        val uid = auth.currentUser?.uid.toString()
        data.shopId = uid
        Log.d(TAG, "registerData: $data")
        firestore.collection("shopdetails").document(uid).set(data)
            .addOnSuccessListener {
                Log.d(TAG, "Data: wrote succeffully ")
                offer(Result.Success(Unit))
            }.addOnFailureListener {
                Log.d(TAG, "Data: error $it")
                offer(Result.Error(it))
            }
        sendFCMRegistrationToServer(null)

    }

    suspend fun sendFCMRegistrationToServer(token:String?) {
        coroutineScope {
            val fuid = auth.currentUser?.uid
            if (token != null){
                val data = hashMapOf("fcmToken" to token)
                firestore.collection(SHOP_DETAILS).document(fuid!!)
                    .set(data, SetOptions.merge())
                    .addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.d(TAG, "sendRegistrationToServer: fails")
                        } else {
                            Log.d(TAG, "sendRegistrationToServer: success")
                        }
                    }
            }

            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.d(TAG, "sendFCMRegistrationToServer: failed")
                } else {
                    val data = hashMapOf("fcmToken" to it.result?.token)
                    firestore.collection(SHOP_DETAILS).document(fuid!!)
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
    }

}