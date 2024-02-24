package com.example.expense_manager_app

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.expense_manager_app.R.id.main_frame
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
//import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var frameLayout: FrameLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.title = "Expense Manager"
        setSupportActionBar(toolbar)

        bottomNavigationView=findViewById(R.id.bottomNavigationbar)
        frameLayout=findViewById(R.id.main_frame)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val NavigationView = findViewById<NavigationView>(R.id.navView)
        NavigationView.setNavigationItemSelectedListener(this)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboard -> {
                    bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.income -> {
                    bottomNavigationView.setItemBackgroundResource(R.color.income_color)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.expense -> {
                    bottomNavigationView.setItemBackgroundResource(R.color.expense_color)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }



    }

    override fun onBackPressed() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    private fun displaySelectedListener(itemId: Int) {
        var fragment: Fragment? = null
        when (itemId) {
            R.id.dashboard -> {return}
            R.id.income -> {return}
            R.id.expense -> {return}
        }
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(main_frame, fragment)
            ft.commit()
        }
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displaySelectedListener(item.itemId)
        return true
    }
}
