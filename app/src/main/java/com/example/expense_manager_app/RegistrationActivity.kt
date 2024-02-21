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
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mEmail: EditText
    private lateinit var mPass: EditText
    private lateinit var btnReg: Button
    private lateinit var mSignin: TextView

    private lateinit var mDialog:ProgressDialog

    //Firebase..
    private lateinit var mAuth: FirebaseAuth


    private fun registration() {
        mEmail = findViewById(R.id.email_reg)
        mPass = findViewById(R.id.password_reg)
        btnReg = findViewById(R.id.btn_reg)
        mSignin = findViewById(R.id.singIn_here)

        btnReg.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val email = mEmail.text.toString().trim()
                val pass = mPass.text.toString().trim()

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email Required..")
                    return
                }
                if (TextUtils.isEmpty(pass)) {
                    mPass.setError("Password Required..")
                }
                mDialog.setMessage("Processing..")
                mDialog.show()

                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mDialog.dismiss()
                        Toast.makeText(applicationContext, "Registration Complete", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext,HomeActivity::class.java))
                    } else {
                        mDialog.dismiss()
                        Toast.makeText(applicationContext, "Registration Failed..", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        })
        mSignin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth=FirebaseAuth.getInstance()
        mDialog=ProgressDialog(this)
        registration()
    }
}
