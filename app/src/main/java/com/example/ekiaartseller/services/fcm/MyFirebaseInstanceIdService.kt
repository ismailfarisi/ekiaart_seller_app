package com.example.ekiaartseller.services.fcm


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ekiaartseller.R
import com.example.ekiaartseller.data.FirebaseSource
import com.example.ekiaartseller.services.OrderListener
import com.example.ekiaartseller.services.fcm.Events.stoken
import com.example.ekiaartseller.ui.newOrderAlertWindow.NewOrderAlertActivity
import com.example.ekiaartseller.util.SHOP_DETAILS
import com.example.ekiaartseller.util.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

object Events {

    var stoken : MutableList<String> = mutableListOf()

}
class MyFirebaseInstanceIdService: FirebaseMessagingService() {


    val orderListener = OrderListener(null, FirebaseSource())


    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken: $token")
        super.onNewToken(token)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            stoken.add(token)
        }else{
            val firestore = FirebaseFirestore.getInstance()
            val data = hashMapOf("fcmToken" to token)
            firestore.collection(SHOP_DETAILS).document(uid)
                .set(data, SetOptions.merge())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d(TAG, "sendRegistrationToServer: fails")
                    } else {
                        Log.d(TAG, "sendRegistrationToServer: success")
                    }
                }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: ")
        val notificationId = 123385
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        val fullScreenIntent = Intent(this,NewOrderAlertActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(this,123,fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(this,"fullscreen")
            .setContentTitle("New Order")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent,true)

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(notificationId, notificationBuilder.build())


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channel = NotificationChannel(
            "fullscreen","new Order",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)

    }


}