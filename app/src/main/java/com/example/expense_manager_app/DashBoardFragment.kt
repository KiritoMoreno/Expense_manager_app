package com.example.expense_manager_app

import android.app.AlertDialog
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

        val mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid)
        val mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid)


        // Connect floating buttons to layout
        fabMainBtn = myView.findViewById(R.id.fb_main_plus_btn)
        fabIncomeBtn = myView.findViewById(R.id.income_ft_btn)
        fabExpenseBtn = myView.findViewById(R.id.expense_ft_btn)

        // Connect floating textviews
        fabIncomeTxt = myView.findViewById(R.id.income_ft_text)
        fabExpenseTxt = myView.findViewById(R.id.expense_ft_text)

        fadeOpen = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_open)
        fadeClose = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_close)

        fabMainBtn.setOnClickListener {
            if (isOpen) {
                closeFabMenu()
            } else {
                openFabMenu()
            }
        }

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
    private fun addData() {
        fabIncomeBtn.setOnClickListener {
            // Lógica para el botón de ingresos
            incomeDataInsert()
        }

        fabExpenseBtn.setOnClickListener {
            // Lógica para el botón de gastos
        }
    }
    fun incomeDataInsert() {
        val mydialog = AlertDialog.Builder(requireActivity())
        val inflater = LayoutInflater.from(requireActivity())
        val myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null)
        mydialog.setView(myview)

        val dialog = mydialog.create()

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

            val ourAmount = amount.toInt()

            if (note.isEmpty()) {
                edtNote.error = "Required Field.."
                return@setOnClickListener
            }
            
            // Cierre del diálogo después de realizar la lógica
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            // Lógica de cancelación si es necesario
            dialog.dismiss()
        }

        dialog.show()
    }

}
