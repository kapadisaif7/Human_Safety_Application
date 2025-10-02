package com.example.keepr_humansafetyapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VideoInterfaceActivity : AppCompatActivity() {
    lateinit var startbtn: Button
    lateinit var pausebtn: Button
    lateinit var endbtn: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_interface)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
            val videoView=findViewById<VideoView>(R.id.videocall)
            val uri=Uri.parse("android.resource://" + packageName + "/"+ R.raw.ronaldo)
            videoView.setVideoURI(uri)
        startbtn=findViewById(R.id.start)
        startbtn.setOnClickListener {
            videoView.start()
        }
        pausebtn=findViewById(R.id.pause)
        pausebtn.setOnClickListener {
            videoView.pause()
        }
        endbtn=findViewById(R.id.ends)
        endbtn.setOnClickListener{
            val explicitintent2=Intent(this,MainActivity::class.java)
            startActivity(explicitintent2)
        }
    }
}
