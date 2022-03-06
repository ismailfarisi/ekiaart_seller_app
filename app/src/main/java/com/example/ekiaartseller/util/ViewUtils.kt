package com.example.ekiaartseller.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import com.example.ekiaartseller.R
import com.example.ekiaartseller.ui.mainView.MainActivity
import com.example.ekiaartseller.ui.auth.AuthActivity

fun Activity.startMainActivity() = Intent(this,
    MainActivity::class.java) .also {
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(it)
    finish()
}
fun  Activity.startLoginActivity() {
    val intent = Intent(this, AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}