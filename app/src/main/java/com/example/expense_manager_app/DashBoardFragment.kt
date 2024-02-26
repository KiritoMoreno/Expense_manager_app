package com.example.expense_manager_app

import Model.Data
import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class DashBoardFragment : Fragment() {

    // Floating buttons
    private lateinit var fabMainBtn: FloatingActionButton
    private lateinit var fabIncomeBtn: FloatingActionButton
    private lateinit var fabExpenseBtn: FloatingActionButton

    // Floating button textviews
    private lateinit var fabIncomeTxt: TextView
    private lateinit var fabExpenseTxt: TextView

    // Boolean
    private var isOpen = false

    // Animations
    private lateinit var fadeOpen: Animation
    private lateinit var fadeClose: Animation

    //Dashboard income and expense result
    private lateinit var  totalIncomeResult: TextView
    private lateinit var  totalExpenseResult: TextView

    //Firebase..
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mIncomeDatabase: DatabaseReference
    private lateinit var mExpenseDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_dash_board, container, false)

        val mAuth = FirebaseAuth.getInstance()
        val mUser: FirebaseUser? = mAuth.currentUser
        val uid: String = mUser?.uid ?: ""

        // En lugar de crear nuevas variables locales, inicializa las propiedades de la clase
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid)
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid)



        // Connect floating buttons to layout
        fabMainBtn = myView.findViewById(R.id.fb_main_plus_btn)
        fabIncomeBtn = myView.findViewById(R.id.income_ft_btn)
        fabExpenseBtn = myView.findViewById(R.id.expense_ft_btn)

        // Connect floating textviews
        fabIncomeTxt = myView.findViewById(R.id.income_ft_text)
        fabExpenseTxt = myView.findViewById(R.id.expense_ft_text)

        //Total income and expense result set..
        totalIncomeResult = myView.findViewById(R.id.income_ft_text)
        totalExpenseResult = myView.findViewById(R.id.expense_ft_text)

        fadeOpen = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_open)
        fadeClose = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_close)

        fabMainBtn.setOnClickListener {
            if (isOpen) {
                closeFabMenu()
            } else {
                openFabMenu()
            }
        }
        // Calculate total income:.
        mIncomeDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalsum = 0
                for (mysnap in dataSnapshot.children) {
                    val data = mysnap.getValue(Data::class.java)
                    totalsum += data?.amount ?: 0
                }

                val stResult = totalsum.toString()
                totalIncomeResult.text = stResult
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the cancellation as needed
            }
        })
        mExpenseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalsum = 0
                for (mysnap in dataSnapshot.children) {
                    val data = mysnap.getValue(Data::class.java)
                    totalsum += data?.amount ?: 0
                }

                val stResult = totalsum.toString()
                totalExpenseResult.text = stResult
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the cancellation as needed
            }
        })

        addData()
        return myView
    }

    private fun openFabMenu() {
        fabIncomeBtn.startAnimation(fadeOpen)
        fabExpenseBtn.startAnimation(fadeOpen)
        fabIncomeBtn.isClickable = true
        fabExpenseBtn.isClickable = true
        fabIncomeTxt.startAnimation(fadeOpen)
        fabExpenseTxt.startAnimation(fadeOpen)
        fabIncomeTxt.isClickable = true
        fabExpenseTxt.isClickable = true
        isOpen = true
    }

    private fun closeFabMenu() {
        fabIncomeBtn.startAnimation(fadeClose)
        fabExpenseBtn.startAnimation(fadeClose)
        fabIncomeBtn.isClickable = false
        fabExpenseBtn.isClickable = false
        fabIncomeTxt.startAnimation(fadeClose)
        fabExpenseTxt.startAnimation(fadeClose)
        fabIncomeTxt.isClickable = false
        fabExpenseTxt.isClickable = false
        isOpen = false
    }
    private fun ftAnimation(){
        if (isOpen) {
            closeFabMenu()
        } else {
            openFabMenu()
        }
    }
    private fun addData() {
        fabIncomeBtn.setOnClickListener {
            // Lógica para el botón de ingresos
            incomeDataInsert()
        }

        fabExpenseBtn.setOnClickListener {
            // Lógica para el botón de gastos
            expenseDataInsert()
        }
    }
    fun incomeDataInsert() {
        val mydialog = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireActivity())
        val myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null)
        mydialog.setView(myview)

        val dialog = mydialog.create()
        dialog.setCancelable(false)

        val edtAmount = myview.findViewById<EditText>(R.id.ammount_edt)
        val edtType = myview.findViewById<EditText>(R.id.type_edt)
        val edtNote = myview.findViewById<EditText>(R.id.note_edt)
        val btnSave = myview.findViewById<Button>(R.id.btnSave)
        val btnCancel = myview.findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            val type = edtType.text.toString().trim()
            val amount = edtAmount.text.toString().trim()
            val note = edtNote.text.toString().trim()

            if (type.isEmpty()) {
                edtType.error = "Required Field.."
                return@setOnClickListener
            }

            if (amount.isEmpty()) {
                edtAmount.error = "Required Field.."
                return@setOnClickListener
            }

            val ourAmountint = amount.toInt()

            if (note.isEmpty()) {
                edtNote.error = "Required Field.."
                return@setOnClickListener
            }
            val id = mIncomeDatabase.push().key
            val mDate = SimpleDateFormat.getDateInstance().format(Date())

            val data = Data(ourAmountint, type, note,id, mDate)
            mIncomeDatabase.child(id!!).setValue(data)
            Toast.makeText(requireActivity(), "Data ADDED", Toast.LENGTH_SHORT).show()

            // Cierre del diálogo después de realizar la lógica
            ftAnimation()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            // Lógica de cancelación si es necesario
            ftAnimation()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun expenseDataInsert(){

        val mydialog = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireActivity())
        val myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null)
        mydialog.setView(myview)

        val dialog = mydialog.create()
        dialog.setCancelable(false)

        val edtAmount = myview.findViewById<EditText>(R.id.ammount_edt)
        val edtType = myview.findViewById<EditText>(R.id.type_edt)
        val edtNote = myview.findViewById<EditText>(R.id.note_edt)
        val btnSave = myview.findViewById<Button>(R.id.btnSave)
        val btnCancel = myview.findViewById<Button>(R.id.btnCancel)

        btnSave.setOnClickListener {
            val type = edtType.text.toString().trim()
            val amount = edtAmount.text.toString().trim()
            val note = edtNote.text.toString().trim()

            if (type.isEmpty()) {
                edtType.error = "Required Field.."
                return@setOnClickListener
            }

            if (amount.isEmpty()) {
                edtAmount.error = "Required Field.."
                return@setOnClickListener
            }

            val ourAmountint = amount.toInt()

            if (note.isEmpty()) {
                edtNote.error = "Required Field.."
                return@setOnClickListener
            }
            //-- check here
            val id = mExpenseDatabase.push().key
            val mDate = SimpleDateFormat.getDateInstance().format(Date())

            val data = Data(ourAmountint, type, note,id, mDate)
            mExpenseDatabase.child(id!!).setValue(data)
            Toast.makeText(requireActivity(), "Data ADDED", Toast.LENGTH_SHORT).show()

            // Cierre del diálogo después de realizar la lógica
            ftAnimation()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            // Lógica de cancelación si es necesario
            ftAnimation()
            dialog.dismiss()
        }

        dialog.show()
    }
}
