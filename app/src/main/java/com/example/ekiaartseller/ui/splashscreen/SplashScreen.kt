package com.example.ekiaartseller.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ekiaartseller.data.FirebaseSource
import com.example.ekiaartseller.data.UserRepository
import com.example.ekiaartseller.ui.auth.AuthActivity
import com.example.ekiaartseller.ui.mainView.MainActivity
import com.example.ekiaartseller.util.startLoginActivity
import com.example.ekiaartseller.util.startMainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class SplashScreen : AppCompatActivity() {
    companion object{
        private const val TAG = "SplashScreenActivity"
    }

    private val repository by lazy { UserRepository(FirebaseSource()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        runBlocking { delay(1000) }
        val currentUser = repository.currentUser()
        Log.d(TAG, "onCreate: $currentUser")
        if (currentUser == null){
            startLoginActivity()
        }else{

            startMainActivity()
        }

    }
}