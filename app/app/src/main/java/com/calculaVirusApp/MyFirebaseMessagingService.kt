package com.calculaVirusApp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val TAG = "My Firebase Message"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        if (p0?.data != null) {
            Log.d(TAG, "Data: " + p0.data.toString())
        }

        if (p0?.notification != null) {
            Log.d(TAG, "Notification: " + p0!!.notification.toString())
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }
}