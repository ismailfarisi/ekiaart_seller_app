package com.example.ekiaartseller.services

import com.example.ekiaartseller.data.FirebaseSource
import com.example.ekiaartseller.data.FirestoreShopSource
import kotlinx.coroutines.ExperimentalCoroutinesApi


class OrderListener(val firestoreShopSource: FirestoreShopSource?, val firebaseSource : FirebaseSource?) {


    @ExperimentalCoroutinesApi
    suspend fun getData()= firestoreShopSource?.getNewOrder()

    suspend fun updateStopStatus(boolean: Boolean) =firestoreShopSource?.updateShopStatus(boolean)

    suspend fun fcmToken(string: String) = firebaseSource?.sendFCMRegistrationToServer(string)


}