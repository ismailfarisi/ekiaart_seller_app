package com.example.ekiaartseller.util

import android.content.Context
import android.content.Intent
import com.example.ekiaartseller.ui.mainView.MainActivity
import com.example.ekiaartseller.ui.auth.AuthActivity

fun Context.startMainActivity() = Intent(this,
    MainActivity::class.java) .also {
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(it)
}
fun  Context.startLoginActivity() {
    val intent = Intent(this, AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}