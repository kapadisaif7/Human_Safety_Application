package com.example.keepr_humansafetyapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class Fakecall_Fragment : Fragment() {

    private lateinit var startFakeCallBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fakecall_, container, false)

        // Initialize button (make sure your fragment_fakecall_.xml has a button with id startFakeCallBtn)
        startFakeCallBtn = view.findViewById(R.id.startFakeCallBtn)

        // Set click listener to start Calling activity
        startFakeCallBtn.setOnClickListener {
            val intent = Intent(requireContext(), Calling::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newInstance() = Fakecall_Fragment()
    }
}
