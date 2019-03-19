package com.example.myapplication.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var memberListAdapter: MemberListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("Navigation item click event handle")
    }

    private fun initView() {
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        memberListAdapter = MemberListAdapter()
        memberListAdapter.addGroupMember("JongSeong")
        memberListAdapter.addGroupMember("HyeonUng")
        memberListAdapter.addGroupMember("SeungPyo")
        memberListAdapter.addGroupMember("KiHyeon")
        memberListAdapter.addGroupMember("JooYeong")

        rcv_main_participants.adapter = memberListAdapter
        rcv_main_participants.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }
}
