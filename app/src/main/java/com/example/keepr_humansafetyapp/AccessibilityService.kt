package com.example.keepr_humansafetyapp

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.content.Intent

class MyAccessibilityService : AccessibilityService() {

    private val volumeReceiver = VolumeButtonReceiver()

    // Required override
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // We don't need to react to normal accessibility events
    }

    // Required override
    override fun onInterrupt() {
        Log.d("MyAccessibilityService", "Service Interrupted ❌")
    }

    // Called when service is enabled by user
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("MyAccessibilityService", "Accessibility Service connected ✅")
    }

    // This lets us capture hardware keys (like Volume)
    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (event.keyCode == KeyEvent.KEYCODE_VOLUME_UP || event.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                Log.d("MyAccessibilityService", "Volume button pressed")
                volumeReceiver.handleVolumeKey(this, event.keyCode)
                return true
            }
        }
        return super.onKeyEvent(event)
    }
}
