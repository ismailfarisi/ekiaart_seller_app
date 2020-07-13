package com.example.ekiaartseller.util

import android.app.Activity
import android.app.AlertDialog
import com.example.ekiaartseller.R

class LoadingDialog (activity: Activity){

    lateinit var alertDialog: AlertDialog
    val activity = activity

    fun startLoadingDialog (){
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog,null))
        builder.setCancelable(true)

        alertDialog = builder.create()
        alertDialog.show()

    }
    fun stopLoadingDialog(){
        alertDialog.dismiss()
    }


}