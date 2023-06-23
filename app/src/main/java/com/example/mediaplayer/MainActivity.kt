package com.example.mediaplayer

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mediaplayer.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.btnSignUp.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                binding.etEmail.text.toString(),
                binding.etPass.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        Toast.makeText(this, task.result.toString(), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MusicPlayer::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                    }
                }
        }

        binding.btnGoogle.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val task= GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful){
                    val account: GoogleSignInAccount?=task.result
                    val credential= GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
//                            Toast.makeText(this,"done", Toast.LENGTH_LONG ).show()
                            startActivity(Intent(this, SignOut::class.java))
                        }else{
                            Toast.makeText(this,"failed", Toast.LENGTH_LONG ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this,"failed", Toast.LENGTH_LONG ).show()
            }

        }
}