package com.example.keepr_humansafetyapp
import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.keepr_humansafetyapp.ContactsModel
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.apply
import kotlin.collections.isNotEmpty
import kotlin.jvm.java
import kotlin.let
import kotlin.text.isEmpty

class EmergencyCallService : Service() {

    private val contacts = mutableListOf<String>()  // Numbers will be filled from Firebase
    private var currentIndex = 0
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var contactsRef: DatabaseReference
    private val firestore = FirebaseFirestore.getInstance()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Fetch contacts from Firestore (match what Contacts_Fragment uses)
        val userId = intent?.getStringExtra("userId") ?: "" // pass uid if available
        if (userId.isEmpty()) {
            Log.e("EmergencyCallService", "No userId passed")
            stopSelf()
            return START_NOT_STICKY
        }

        firestore.collection("users")
            .document(userId)
            .collection("contacts")
            .get()
            .addOnSuccessListener { snapshot ->
                contacts.clear()
                for (doc in snapshot.documents) {
                    val phone = doc.getString("phone")
                    phone?.let { contacts.add(it) }
                }
                if (contacts.isNotEmpty()) {
                    currentIndex = 0
                    makeCall()
                } else {
                    Log.e("EmergencyCallService", "No contacts found")
                    stopSelf()
                }
            }
            .addOnFailureListener { e ->
                Log.e("EmergencyCallService", "Failed to fetch contacts: ${e.message}")
                stopSelf()
            }

        // Make this a foreground service if needed (createNotification() function)
        startForeground(1, createNotification())

        return START_NOT_STICKY
    }

    private fun makeCall() {
        if (currentIndex >= contacts.size) {
            stopSelf()
            return
        }

        val number = contacts[currentIndex]
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")).apply {
            Intent.setFlags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        // Must have CALL_PHONE permission granted beforehand (request from Activity)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            try {
                startActivity(callIntent)
                Log.d("EmergencyCallService", "Started call to $number")
            } catch (se: SecurityException) {
                Log.e("EmergencyCallService", "SecurityException launching call", se)
                stopSelf()
                return
            }
        } else {
            Log.e("EmergencyCallService", "CALL_PHONE permission not granted")
            stopSelf()
            return
        }

        // retry next contact after 10s
        Handler(Looper.getMainLooper()).postDelayed({
            currentIndex++
            if (currentIndex < contacts.size) makeCall() else stopSelf()
        }, 10_000)
    }
    private fun createNotification(): Notification {
        val channelId = "emergency_call_service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Emergency Call Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Emergency Service")
            .setContentText("Preparing to call emergency contacts")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }


    private val phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    // Call answered → close FakeCallActivity
                    sendBroadcast(Intent("CLOSE_FAKECALL"))
                }
                TelephonyManager.CALL_STATE_IDLE -> {
                    // Call ended → close FakeCallActivity
                    sendBroadcast(Intent("CLOSE_FAKECALL"))
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onDestroy() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
