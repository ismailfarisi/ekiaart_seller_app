package com.example.ekiaartseller.util

import android.content.Context
import android.content.Intent
import com.example.ekiaartseller.MainActivity
import com.example.ekiaartseller.auth.AuthActivity

fun Context.startMainActivity() {
    val intent = Intent(this,MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}
fun  Context.startLoginActivity() {
    val intent = Intent(this,AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}