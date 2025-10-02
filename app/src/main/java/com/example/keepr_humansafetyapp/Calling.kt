package com.example.keepr_humansafetyapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Calling : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var getaudio: Button
    private lateinit var endbtn: Button
    lateinit var vidbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calling)

        // Initialize buttons AFTER setContentView
        getaudio = findViewById(R.id.getaudio)
        endbtn = findViewById(R.id.end)
        vidbtn=findViewById(R.id.Videobtn)

        // Initialize MediaPlayer AFTER setContentView and use `this`
        mediaPlayer = MediaPlayer.create(this, R.raw.kon)

        // Optional: handle edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getaudio.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        endbtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
                val homeintent=Intent(this,MainActivity::class.java)
                startActivity(homeintent)
            }
        }
        vidbtn.setOnClickListener{
            //explicit intent is used to naviigate through different components by clicking on the buttons
            val explicitintent= Intent(this,VideoInterfaceActivity::class.java)
            startActivity(explicitintent)   //to start the execution of the activity
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}

