package com.example.ekiaartseller.ui.mainView

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ekiaartseller.R
import com.example.ekiaartseller.databinding.ActivityMainBinding
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.services.MyService
import com.example.ekiaartseller.services.fcm.Events
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val view = binding.root
        setContentView(view)
        checkStatus()

        binding.onlineBtn.setOnClickListener {
            onlineSwitchPressed(it)
        }


        val bottomNavigationView = binding.navBar
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnNavigationItemReselectedListener { /* Do Nothing*/ }

        sendFCMTokentoFirestore()



    }

    private fun sendFCMTokentoFirestore() {
        if (Events.stoken.isNullOrEmpty()){
            Log.d(com.example.ekiaartseller.util.TAG, "sendFCMTokentoFirestore: null")
            return
        }
        viewModel.sendFCMtoken(Events.stoken.last())
    }


    private fun onlineSwitchPressed(view: View) {
        if (binding.onlineBtn.isChecked){
            updateShopStatus(START_SERVICE)
        }else{
            updateShopStatus(STOP_SERVICE)
        }
    }
    private fun updateShopStatus(boolean: Boolean){
        viewModel.updateShopStatus(boolean).observe(this, Observer { result ->
            when(result){
                is Result.Success -> {startOrStopForegroundService(boolean)
                    binding.progressBar.visibility = View.GONE
                    binding.onlineBtn.visibility =View.VISIBLE
                    if (boolean == START_SERVICE){
                        binding.onlineBtn.setText(R.string.online_btn_txt)
                    }else binding.onlineBtn.setText(R.string.offline_btn_txt)
                }
                is Result.Error -> {Toast.makeText(this,"failed to go online",Toast.LENGTH_SHORT).show()}
                is Result.Loading -> {binding.onlineBtn.visibility =View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
    private fun startOrStopForegroundService(boolean: Boolean){
        if (boolean == START_SERVICE){
            val intent = Intent(this,MyService::class.java)
            intent.action = MyService.ACTION_START_FOREGROUND_SERVICE
            startService(intent)
        }else if(boolean == STOP_SERVICE){
            val intent = Intent(this,MyService::class.java)
            intent.action = MyService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
        }
    }
    private fun checkStatus(){
        lifecycleScope.launch {
            viewModel.checkStopStatus().collect { result ->
                when(result){
                    is Result.Success -> {
                        if (result.data){ binding.onlineBtn.isChecked = true }

                    }
                }
            }
        }
    }



    companion object{
        const val START_SERVICE =true
        const val STOP_SERVICE =false
    }
}