package com.example.expense_manager_app

import Model.Data
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ExpenseFragment : Fragment() {

    // Firebase database
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mExpenseDatabase: DatabaseReference

    // Recycler view
    private lateinit var recyclerView: RecyclerView

    private lateinit var expenseTotalSum: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myview: View = inflater.inflate(R.layout.fragment_expense, container, false)
        mAuth = FirebaseAuth.getInstance()

        val mUser: FirebaseUser? = mAuth.currentUser
        val uid: String = mUser?.uid ?: ""

        mExpenseDatabase =
            FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid)

        expenseTotalSum = myview.findViewById(R.id.expense_txt_result)
        recyclerView = myview.findViewById(R.id.recycler_id_expense)

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        mExpenseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var totalValue = 0

                for (mySnapshot in dataSnapshot.children) {
                    val data = mySnapshot.getValue(Data::class.java)
                    data?.let {
                        totalValue += it.amount
                    }

                }
                val stTotalValue = totalValue.toString()

                expenseTotalSum.setText(stTotalValue)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja la cancelación como desees
            }
        })
        return myview
    }

    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Data>()
            .setQuery(mExpenseDatabase, Data::class.java)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.expense_recycler_data, parent, false)
                return MyViewHolder(view)
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Data) {
                holder.setType(model.type ?: "")
                holder.setNote(model.note ?: "")
                holder.setDate(model.date ?: "")
                holder.setAmount(model.amount ?: 0)
            }
        }

        recyclerView.adapter = adapter

        adapter.startListening()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mView: View = itemView

        fun setType(type: String) {
            val mType: TextView = mView.findViewById(R.id.type_txt_expense)
            mType.text = type
        }

        fun setNote(note: String) {
            val mNote: TextView = mView.findViewById(R.id.note_txt_expense)
            mNote.text = note
        }

        fun setDate(date: String) {
            val mDate: TextView = mView.findViewById(R.id.date_txt_expense)
            mDate.text = date
        }

        fun setAmount(amount: Int) {
            val mAmmount: TextView = mView.findViewById(R.id.ammount_txt_expense)
            val stAmmount: String = amount.toString()
            mAmmount.text = stAmmount
        }
    }
}