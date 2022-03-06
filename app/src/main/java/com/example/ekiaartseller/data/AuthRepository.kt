package com.example.ekiaartseller.data

import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.domain.ShopDetails
import com.google.firebase.auth.PhoneAuthCredential

class UserRepository(private val firebaseSource: FirebaseSource) {

    fun currentUser() = firebaseSource.currentUser()
    suspend fun signInWithPhone(credential: PhoneAuthCredential) : Result<Boolean> = firebaseSource.signInWithPhoneAuthCredential(credential)
    suspend fun registerData(data: ShopDetails) = firebaseSource.registerData(data)
}