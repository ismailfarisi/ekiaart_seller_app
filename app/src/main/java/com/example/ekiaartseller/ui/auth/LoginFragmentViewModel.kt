package com.example.ekiaartseller.ui.auth

import android.app.Activity
import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModel
import android.util.Log
import com.example.ekiaartseller.data.ShopDetails
import com.example.ekiaartseller.ui.interface1.IAuth
import com.example.ekiaartseller.ui.interface1.IAuthLogin
import com.example.ekiaartseller.ui.interface1.IDataUpdated
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit


class LoginFragmentViewModel  : ViewModel() {




    private val auth :FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var  storedVerificationId :String
    lateinit var resendToken :PhoneAuthProvider.ForceResendingToken
    lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var iAuth : IAuth? = null
    var iAuthLogin : IAuthLogin? =null
    var iDataUpdated : IDataUpdated? = null


    val currentUser = auth.currentUser
    fun registerData(data: ShopDetails){
        val v = data.toString()
        val uid = auth.currentUser?.uid.toString()

        Log.d(TAG, "registerData: $v")
        firestore.collection("shopdetails").document(uid).set(data)
            .addOnSuccessListener {
                Log.d(TAG, "Data: wrote succeffully ")
                iDataUpdated?.onSuccess()
            }.addOnFailureListener {
                Log.d(TAG, "Data: error $it")
                iDataUpdated?.onFailure()
            }
    }


    fun login(phno1: String,context:Activity){
       Log.d(TAG, "login: $phno1")


        verificationCallBacks()
        try {

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phno1,
                60,
                TimeUnit.SECONDS,
                context, callbacks
            )

            Log.d(TAG, "login: ph")
        }catch (e:Exception){

        }

   }

    private fun verificationCallBacks(){
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                storedVerificationId = p0
                Log.d(TAG, "onCodeSent: $storedVerificationId")
                iAuthLogin?.onCodeSent()
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted: ")
                try {
                    signInWithPhoneAuthCredential(credential)
                }catch (e:FirebaseException){}
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(TAG, "onVerificationFailed: ")
                if (e is FirebaseAuthInvalidCredentialsException){
                    iAuthLogin?.verificationFailed()
                }
            }

        }
    }
   


    fun verifyCode(code: String){
        Log.d(TAG, "verifyCode: $code vercode : $storedVerificationId ")


        try {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        }catch (e:Exception){

        }
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user


                    Log.d(TAG, "detail exist:  uid :${user?.uid}")
                    if (task.result?.additionalUserInfo!!.isNewUser ){
                        iAuth?.newUserRegister()
                    }else {
                        checkShopDetailExist(user!!)
                    }
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
    }
    private fun checkShopDetailExist(user : FirebaseUser){
        val uid = user.uid

        firestore.collection("shopdetails").document(uid).get().addOnSuccessListener {
            val y = it.exists()
            Log.d(TAG, "checkShopDetailExist: ${y}")
            if (y){
                iAuth?.loginSuccess()
            }else{
                iAuth?.newUserRegister()
            }
        }



    }
}


