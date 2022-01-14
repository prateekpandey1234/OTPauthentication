package com.example.internproject1.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.internproject1.R
import kotlinx.android.synthetic.main.form_layout.*

class FormFragment:Fragment(R.layout.form_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button4.setOnClickListener{
            findNavController().navigate(R.id.action_formFragment_to_appointmentFragment)
        }
    }
}