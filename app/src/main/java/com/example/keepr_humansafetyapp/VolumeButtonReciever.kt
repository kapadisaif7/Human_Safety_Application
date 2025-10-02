package com.example.keepr_humansafetyapp

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.keepr_humansafetyapp.EmergencyCallService
import kotlin.jvm.java

class VolumeButtonReceiver {

    private var lastPressTime = 0L
    private var pressCount = 0

    fun handleVolumeKey(context: Context, keyCode: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPressTime > 1500) {
            pressCount = 0
        }
        pressCount++
        lastPressTime = currentTime

        if (pressCount == 3) {
            // ✅ Triple volume press detected

            val serviceIntent = Intent(context, EmergencyCallService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)

            pressCount = 0
        }
    }
}
