package com.example.ekiaartseller.ui.mainView

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.ekiaartseller.data.FirestoreShopSource
import com.example.ekiaartseller.data.ShopRepository
import com.example.ekiaartseller.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivityViewModel() : ViewModel() {
    @ExperimentalCoroutinesApi
    private val shopRepository: ShopRepository = FirestoreShopSource()
    @ExperimentalCoroutinesApi
    fun updateShopStatus(boolean: Boolean) : LiveData<Result<Unit>> = liveData(Dispatchers.IO) {
        emitSource(shopRepository.updateShopStatus(boolean).asLiveData())
    }
    suspend fun checkStopStatus() = shopRepository.checkShopStatus()
    fun sendFCMtoken(token : String) = shopRepository.sendFCMRegistrationToServer(token)
}