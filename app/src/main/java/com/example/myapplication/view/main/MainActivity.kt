package com.example.myapplication.view.main

import android.content.Intent
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
import com.example.myapplication.view.calendar.CalendarActivity
import com.example.myapplication.view.chat.ChatInfoListAdapter
import com.example.myapplication.view.chat.ChatViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(), NavigationView.OnNavigationItemSelectedListener,GroupChangeListener {
    override val TAG: String
        get() = MainActivity::class.java.simpleName
    private lateinit var memberListAdapter: MemberListAdapter
    private lateinit var fragment:TeamListFragment
    private lateinit var chatViewModel: ChatViewModel

    override fun getViewModel(dataSource: DataSource): MainViewModel {
        return MainViewModel(dataSource)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMenu()

        initView()

        includeInit()


        fragment=TeamListFragment()

        spinner_show_group.setOnClickListener {
            val transaction=supportFragmentManager.beginTransaction()
            transaction.apply {
                replace(R.id.fragment_show_team,fragment)
                commit()
            }
        }
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


        chatViewModel = ChatViewModel(AppInitialize.dataSource)
        binding.chatviewmodel = chatViewModel
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

    private fun includeInit() {
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

            iv_notice.setOnClickListener {
                //TODO:공지사항 visible unvisible
            }

            iv_vote.setOnClickListener {
                //TODO:투표화면 이동
            }

        }
    }

    private fun initMenu() {
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

        btn_main_schedule.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        btn_main_references.setOnClickListener {
            //TODO:자료실 화면 이동
        }

    }

    override fun change(groupID: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
