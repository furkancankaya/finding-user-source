package com.coxjepsy.radon

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

/**
 * Activity to demonstrate anonymous login and account linking (with an email/password account).
 */
open class AnonymousAuthActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
        initializeDbRef()
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
        signInAnonymously()
    }
    // [END on_start_check_user]


    // [START declare_database_ref]
    private lateinit var database: DatabaseReference
    // [END declare_database_ref]
    private lateinit var referrerClient: InstallReferrerClient

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = FirebaseDatabase.getInstance().reference
        // [END initialize_database_ref]
    }



    private fun signInAnonymously() {
        // [START signin_anonymously]
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success")
                    val user = auth.currentUser
                    var utmsource= "empty"
                    updateUI(user)
                    referrerClient = InstallReferrerClient.newBuilder(this).build()
                    referrerClient.startConnection(object : InstallReferrerStateListener {
                        override fun onInstallReferrerServiceDisconnected() {
                            // Try to restart the connection on the next request to
                            // Google Play by calling the startConnection() method.
                        }
                        override fun onInstallReferrerSetupFinished(responseCode: Int) {
                            when (responseCode) {
                                InstallReferrerClient.InstallReferrerResponse.OK -> {

                                    val response: ReferrerDetails = referrerClient.installReferrer
                                    val referrerUrl: String = response.installReferrer
                                    val referrerClickTime: Long = response.referrerClickTimestampSeconds
                                    val appInstallTime: Long = response.installBeginTimestampSeconds
                                    val instantExperienceLaunched: Boolean = response.googlePlayInstantParam
                                    // Connection established.
                                    Log.w(TAG, "OK"+referrerUrl)
                                    Toast.makeText(
                                        baseContext, "OKK"+referrerUrl,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val referrers = referrerUrl.split("&")
                                    for (pair: String in referrers) {
                                        val key: String = pair.split("=")[0]
                                        val value: String = pair.split("=")[1]
                                        if (key.equals("utm_source")) {
                                            utmsource = value
                                            break
                                        }
                                    }

                                    registerUser(user,utmsource, response )


                                }
                                InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                                    // API not available on the current Play Store app.
                                    Log.w(TAG, "FEATURE_NOT_SUPPORTED")
                                    Toast.makeText(
                                        baseContext, "FEATURE_NOT_SUPPORTED",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    registerUser(user,utmsource)
                                }
                                InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                                    // Connection couldn't be established.
                                    Log.w(TAG, "SERVICE_UNAVAILABLE")
                                    Toast.makeText(
                                        baseContext, "AnonymousRegistiratedFailed!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    registerUser(user,utmsource)
                                }
                            }
                        }
                    })


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
        // [END signin_anonymously]


    }



    private fun registerUser(user:FirebaseUser?, utmsource:String, referral:ReferrerDetails?=null){
        val x = User(
            uid = user!!.uid,
            creationTimestamp = user.metadata!!.creationTimestamp ,
            utm_source = utmsource,
            referral = referral
        )

        database.child("users").child(user!!.uid).setValue(x,DatabaseReference.CompletionListener {error,ref ->
            if (error!=null){
                Log.w(TAG, "signInAnonymously:failure"+ error.message)
                Toast.makeText(baseContext, "AnonymousRegistiratedFailed!!",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun linkAccount() {
        // Create EmailAuthCredential with email and password
        val credential = EmailAuthProvider.getCredential("", "")
        // [START link_credential]
        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "linkWithCredential:success")
                    val user = task.result?.user
                    updateUI(user)
                } else {
                    Log.w(TAG, "linkWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END link_credential]
    }


    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "AnonymousAuth"
    }
}