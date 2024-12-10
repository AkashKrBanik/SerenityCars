package com.example.serenitycars

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.serenitycars.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        binding.btnSignup.setOnClickListener {
            val fullName = binding.regName.text.toString()
            val mobileNumberStr = binding.regMobile.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()

            if (fullName.isNotEmpty() && mobileNumberStr.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val mobileNumber: Long? = mobileNumberStr.toLongOrNull()
                if (mobileNumber != null && mobileNumber.toString().length == 10) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val userId = firebaseAuth.currentUser?.uid
                                val user = UserData(userId, fullName, mobileNumber, email, password)
                                userId?.let {
                                    databaseReference.child(it).setValue(user)
                                }
                                Toast.makeText(this@SignupActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@SignupActivity, "Signup Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this@SignupActivity, "Mobile number must be exactly 10 digits", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@SignupActivity, "All fields are mandatory!!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fabBack.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }

        binding.signinRedirect.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }
    }
}
