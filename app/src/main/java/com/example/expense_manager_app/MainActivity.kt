package com.example.expense_manager_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mEmail: EditText
    private lateinit var mPass: EditText
    private lateinit var btnLogin: Button
    private lateinit var mForgetPassword: TextView
    private lateinit var mSignupHere: TextView

    private lateinit var mDialog:ProgressDialog
    //Firebase..
    private lateinit var mAuth:FirebaseAuth


    private fun loginDetails() {

        mEmail = findViewById(R.id.email_login)
        mPass = findViewById(R.id.password_login)
        btnLogin = findViewById(R.id.btn_login)
        mForgetPassword = findViewById(R.id.forget_password)
        mSignupHere = findViewById(R.id.singup_reg)

        btnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val email = mEmail.text.toString().trim()
                val pass = mPass.text.toString().trim()

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email Required..")
                    return
                }
                if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Password Required..")
                    return
                }
                mDialog.setMessage("Processing..")
                mDialog.show()

                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mDialog.dismiss()
                        startActivity(Intent(applicationContext,HomeActivity::class.java))
                        Toast.makeText(applicationContext, "Login Successful..", Toast.LENGTH_SHORT).show()

                    } else {
                        mDialog.dismiss()
                        Toast.makeText(applicationContext, "Login Failed..", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        // Registration activity
        mSignupHere.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                startActivity(Intent(applicationContext, RegistrationActivity::class.java))
            }
        })

        // Reset password activity
        mForgetPassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                startActivity(Intent(applicationContext, ReseatActivity::class.java))
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth=FirebaseAuth.getInstance()
        mDialog=ProgressDialog(this)
        loginDetails()
    }
}
