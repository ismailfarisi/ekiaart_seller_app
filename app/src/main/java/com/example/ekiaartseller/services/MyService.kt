package com.example.ekiaartseller.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.ekiaartseller.R
import com.example.ekiaartseller.data.FirestoreShopSource
import com.example.ekiaartseller.domain.Result
import com.example.ekiaartseller.ui.newOrderAlertWindow.NewOrderAlertActivity
import com.example.ekiaartseller.util.TAG
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect


@ExperimentalCoroutinesApi
class MyService : LifecycleService(){

    companion object {

        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"

        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"

        private const val CHANNEL_ID: String = "1001"

        private const val CHANNEL_NAME: String = "Event Tracker"

        private const val SERVICE_ID: Int = 1
    }

    private lateinit var orderListener: OrderListener




    @ExperimentalCoroutinesApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        orderListener = OrderListener(FirestoreShopSource(),null)
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    ACTION_START_FOREGROUND_SERVICE -> startForegroundService()
                    ACTION_STOP_FOREGROUND_SERVICE ->  stopForegroundService()

                }
            }

        }
        return super.onStartCommand(intent, flags, startId)

    }


    @ExperimentalCoroutinesApi
    private fun startForegroundService() {
        CoroutineScope(Dispatchers.IO).launch {
            orderListener.getData()?.collect { orderResult ->
                when (orderResult) {
                    is Result.Success -> {
                        val order = orderResult.data
                        Log.d(TAG, "startForegroundService: $order")
                        val intent = Intent(this@MyService, NewOrderAlertActivity::class.java)
                        intent.putExtra("dd", order)
                        intent.flags =Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Ekiaart Seller")
            .setContentText("you are online")

        startForeground(SERVICE_ID,notificationBuilder.build())
    }


    private fun stopForegroundService() {
        onDestroy()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(channel)

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")

    }


    override fun onDestroy() {
        CoroutineScope(Dispatchers.IO).launch {
            orderListener.updateStopStatus(false)?.collect {
                when(it){
                    is Result.Success -> {
                        Log.d(TAG, "onDestroy: successfully updated")}
                    is Result.Error -> Log.d(TAG, "onDestroy: updation failed")
                }
            }
        }
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }



}
