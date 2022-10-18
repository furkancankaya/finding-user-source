package com.coxjepsy.radon

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.coxjepsy.radon.ui.theme.RadonTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import com.coxjepsy.radon.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

//import com.google.firebase.analytics.ktx.analytics
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
//import java.util.Objects


//DYNAMICLINKS
// Dynamic Links will start with https://radon.com
//"appAssociation": "AUTO",
//"rewrites": [ { "source": "/**", "dynamicLinks": true } ]

//google-site-verification=2hRVWMpqLveiE7GFf8YMWKW0KKyzZ0-uEWDzp8vzX4A


class MainActivity : AnonymousAuthActivity() {

    private lateinit var auth: FirebaseAuth


    //realtime database
    private lateinit var binding: ActivityMainBinding
    private lateinit var database : DatabaseReference
    //realtime database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //realtime database
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)
       // binding.registerBnt.setOnClickListener {
            //val link = binding.realtime.text.toString()
            database = FirebaseDatabase.getInstance().getReference("link")

        //realtime database

        // [START initialize_auth]
        // Initialize Firebase Auth

        auth = Firebase.auth
        // [END initialize_auth]

        // [START on_start_check_user]
        //override fun onStart() {
        //super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        // val currentUser = auth.currentUser
        //if(currentUser != null){
        //reload();
        // }
        //}
        // [END on_start_check_user]
        setContent {
            RadonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Neye baktın!!")
                }
            }
        }



        //firestore = FirebaseFirestore.getInstance();
        //Map<String,Object> user = new HashMap<>();
        //user.put
    }

}

@Composable
fun Greeting(name: String) {
    Surface(color= Color.DarkGray) {
        Text(text = "Hayırdır birader $name!", modifier = Modifier.padding(130.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RadonTheme {
        Greeting("Neye baktın")
    }
}
