package com.example.myapplication.view.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.chat.ChatInfoListAdapter
import com.example.myapplication.view.chat.ChatViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(), NavigationView.OnNavigationItemSelectedListener {
    override val TAG: String
        get() = MainActivity::class.java.simpleName
    private lateinit var memberListAdapter: MemberListAdapter
    private lateinit var chatViewModel: ChatViewModel

    override fun getViewModel(dataSource: DataSource): MainViewModel {
        return MainViewModel(dataSource)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        includeInit()
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
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_menu)

        nav_view.setNavigationItemSelectedListener(this)



        chatViewModel= ChatViewModel(AppInitialize.dataSource)
        binding.chatviewmodel = chatViewModel
        chatViewModel.loadChatinfoList()    //나중에 채팅방 ID 파라미터 넣어야됨
        chatViewModel.receiveMessage()


        memberListAdapter = MemberListAdapter()
        memberListAdapter.addGroupMember("JongSeong")
        memberListAdapter.addGroupMember("HyeonUng")
        memberListAdapter.addGroupMember("SeungPyo")
        memberListAdapter.addGroupMember("KiHyeon")
        memberListAdapter.addGroupMember("JooYeong")

        rcv_main_participants.adapter = memberListAdapter
        rcv_main_participants.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    private fun includeInit(){
        include_chat.apply {

            button_send.setOnClickListener {
                chatViewModel.sendButtonClicked(et_messagebox.text.toString())
                et_messagebox.text?.clear()
            }

            rv_chat.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rv_chat.adapter = ChatInfoListAdapter()

            iv_show_toolbox.setOnClickListener {
                chatViewModel.showToolBox()
            }

        }


    }
}
