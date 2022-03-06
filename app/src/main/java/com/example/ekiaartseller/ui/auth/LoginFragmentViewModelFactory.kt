package com.example.ekiaartseller.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ekiaartseller.data.UserRepository
import com.example.ekiaartseller.ui.interface1.IAuth
import com.example.ekiaartseller.ui.interface1.IAuthLogin
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class LoginFragmentViewModelFactory (
    private val repository: UserRepository,
    private val iAuthLogin: IAuthLogin?
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginFragmentViewModel::class.java)){
            return LoginFragmentViewModel(repository,iAuthLogin) as T
        }
        throw IllegalArgumentException("view")
    }
}