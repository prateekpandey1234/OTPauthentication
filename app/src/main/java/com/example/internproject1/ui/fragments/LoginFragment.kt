package com.example.internproject1.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.internproject1.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.login_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.concurrent.TimeUnit

class LoginFragment:Fragment(R.layout.login_layout) {
    lateinit var number:String
    lateinit var auth: FirebaseAuth
    lateinit var verificationId2: String
    lateinit var codeverify:String
    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("RUNNING", "onVerificationCompleted:$credential")
        }
        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }
            // Show a message and update the UI
        }
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("RUNNING", "onCodeSent:$verificationId+$token")
            verificationId2=verificationId
            // Save verification ID and resending token so we can use them later
        }

        override fun onCodeAutoRetrievalTimeOut(VerificationId: String) {
            super.onCodeAutoRetrievalTimeOut(VerificationId)
            //this method here is called for the devices without the SIM card which don't have auto-verification management
            Log.d("autoretrival","$VerificationId")
        }}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button3.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_appointmentFragment)
        }

        button.setOnClickListener{
            number = "+91"+editTextTextPersonName2.text.toString()
            sendOTP(number)
        }
        button2.setOnClickListener{
            codeverify= editTextTextPersonName3.text.toString()
            verifyPhoneNumberWithCode(verificationId2 ,codeverify)
        }
    }
    private fun sendOTP(phone:String){
        auth.setLanguageCode("en")
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("RUNNING", "signInWithCredential:success")

                        val user = task.result?.user
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w("RUNNING", "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }.await()
            }catch(e:Exception){
                Log.d("RUNNING","coroutine$e")
            }
//

        }

    }
    private fun verifyPhoneNumberWithCode( verificationId:String,code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        Log.d("RUNNING","$verificationId")
        signInWithPhoneAuthCredential(credential)
        // [END verify_with_code]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        }
}