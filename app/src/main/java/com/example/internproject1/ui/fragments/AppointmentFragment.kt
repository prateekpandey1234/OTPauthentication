package com.example.internproject1.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.internproject1.R
import kotlinx.android.synthetic.main.appointment_layout.*

class AppointmentFragment:Fragment(R.layout.appointment_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener{
            findNavController().navigate(R.id.action_appointmentFragment_to_formFragment)
        }
    }
}