package com.example.ekiaartseller.ui.newOrderAlertWindow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ekiaartseller.databinding.ActivityNewOrderAlertBinding
import com.example.ekiaartseller.domain.OrderDetails
import com.example.ekiaartseller.ui.mainView.MainActivity
import com.example.ekiaartseller.util.TAG

class NewOrderAlertActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewOrderAlertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewOrderAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<OrderDetails>("dd")
        Log.d(TAG, "NewOrederAlertActivity $data")
        binding.acceptBtn.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        binding.rejectBtn.setOnClickListener {
            this.finish()
        }

    }

}