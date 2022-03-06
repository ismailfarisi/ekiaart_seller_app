package com.example.ekiaartseller.ui.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.domain.ShopDetails
import com.example.ekiaartseller.data.UserRepository
import com.example.ekiaartseller.ui.interface1.IAuth
import com.example.ekiaartseller.ui.interface1.IAuthLogin
import com.example.ekiaartseller.util.TAG
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class LoginFragmentViewModel(
    private val repository: UserRepository,
    private val iAuthLogin: IAuthLogin?
)  : ViewModel() {


    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    var iAuth:IAuth? = null






    fun loginPhoneOtpRequest(phno1: String, context: Activity) {
        val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
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
                    viewModelScope.launch(Dispatchers.IO) { loginProgress(credential) }
                } catch (e: FirebaseException) {
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d(TAG, "onVerificationFailed: ")
                if (e is FirebaseAuthInvalidCredentialsException) {
                    iAuthLogin?.verificationFailed()
                }
            }

        }
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phno1,
                60,
                TimeUnit.SECONDS,
                context, callbacks
            )
        } catch (e: Exception) {

        }
    }


    fun verifyCode(code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            viewModelScope.launch(Dispatchers.IO) { loginProgress(credential) }
        } catch (e: Exception) {

        }
    }
    fun regiterData(data : ShopDetails){
        viewModelScope.launch(Dispatchers.IO) { repository.registerData(data) }

    }

    suspend fun loginProgress (credential: PhoneAuthCredential){
        val result = repository.signInWithPhone(credential)
        when(result){
            is Result.Success -> {
                when(result.data){
                    true ->  iAuth?.loginSuccess()
                    false -> iAuth?.newUserRegister()
                }
            }
            is Result.Error -> iAuth?.loginFailed()
        }
    }




}


