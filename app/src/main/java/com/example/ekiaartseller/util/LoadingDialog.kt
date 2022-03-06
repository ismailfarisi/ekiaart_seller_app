package com.example.ekiaartseller.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.ekiaartseller.databinding.LoadingDialogBinding

class LoadingDialog (context: Context){

    lateinit var alertDialog: AlertDialog
    val context = context
    private lateinit var binding : LoadingDialogBinding
    fun startLoadingDialog (){
        val builder = AlertDialog.Builder(context)
        binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        builder.setCancelable(true)

        alertDialog = builder.create()
        alertDialog.show()

    }
    fun stopLoadingDialog(){
        alertDialog.dismiss()
    }


}