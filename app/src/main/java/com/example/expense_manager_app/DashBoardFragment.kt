package com.example.expense_manager_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myView = inflater.inflate(R.layout.fragment_dash_board, container, false)

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
}
