package com.coxjepsy.radon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

abstract class FirebaseUIActivity : AnonymousAuthActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_ui)
    }

    protected fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...



            val database= FirebaseDatabase.getInstance().reference
            database.child("users").child(user!!.uid).child("e-mail").get().addOnSuccessListener {
                if (it.value==null){
                    database.child("users").child(user!!.uid).child("e-mail").setValue(user.email,DatabaseReference.CompletionListener{error,ref ->
                      if (error == null) {
                          database.child("users").child(user!!.uid).child("utm_source").get()
                              .addOnSuccessListener {
                                  database.child("usercounth").child("${it.value}").child("guest")
                                      .setValue(ServerValue.increment(-1))
                                  database.child("usercounth").child("${it.value}").child("email")
                                      .setValue(ServerValue.increment(1))
                              }
                      }})
                }

            }





        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}