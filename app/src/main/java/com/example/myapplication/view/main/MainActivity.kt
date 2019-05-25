package com.example.myapplication.view.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Point
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.view.addfriends.AddFriendsDialog
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.calendar.CalendarActivity
import com.example.myapplication.view.chat.ChatInfoListAdapter
import com.example.myapplication.view.chat.ChatViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    NavigationView.OnNavigationItemSelectedListener, GroupChangeListener,ChatRoomChangeListener, MemberClickListener, MainNavigator {
    override val TAG: String
        get() = MainActivity::class.java.simpleName
    private lateinit var memberListAdapter: MemberListAdapter
    private lateinit var fragment: TeamListFragment
    private lateinit var chatViewModel: ChatViewModel
    var receiver:BroadcastReceiver = object:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.updateChatList()
        }
    }

    override fun getViewModel(dataSource: DataSource): MainViewModel {
        return MainViewModel(dataSource,this)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMenu()

        initView()

        includeInit()


        fragment = TeamListFragment()

        spinner_show_group.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.apply {
                replace(R.id.fragment_show_team, fragment)
                commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter("updateChatList"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
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

    override fun setChatViewModel(chatRoom: ChatRoom?) {
        chatViewModel = ChatViewModel(AppInitialize.dataSource,chatRoom ?: ChatRoom("",""))
        binding.chatviewmodel=chatViewModel
        include_chat.rv_chat.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        include_chat.rv_chat.adapter = ChatInfoListAdapter(chatViewModel)
        chatViewModel.loadChatinfoList()
        chatViewModel.receiveMessage()
    }

    private fun initView() {
        binding.mainviewmodel = viewModel

        rcv_main_participants.adapter = MemberListAdapter(this)
        rcv_main_participants.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.rvChat.adapter = ChatListAdapter(this)
        binding.rvChat.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        viewModel.init()
    }

    private fun includeInit() {
        include_chat.apply {

            button_send.setOnClickListener {
                chatViewModel.sendButtonClicked(et_messagebox.text.toString())
                et_messagebox.text?.clear()
            }

            iv_show_toolbox.setOnClickListener {
                chatViewModel.showToolBox()
            }

            iv_notice.setOnClickListener {
                chatViewModel.showNotice()
            }

            iv_vote.setOnClickListener {
                //TODO:투표화면 이동
            }

            iv_add_friend.setOnClickListener {
                val dialog = AddFriendsDialog(context)
                dialog.show()

                val display = windowManager.defaultDisplay
                val point = Point()
                display.getSize(point)

                val window = dialog.window
                val x = point.x*0.6f
                val y = point.y*0.4f

                window.setLayout(x.toInt(),y.toInt())
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

    override fun groupChanged(groupID: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun chatRoomChagned(chatRoom: ChatRoom) {
        chatViewModel.onDestroy()
        chatViewModel = ChatViewModel(AppInitialize.dataSource,chatRoom)
        binding.chatviewmodel=chatViewModel
        val adapter = include_chat.rv_chat.adapter as ChatInfoListAdapter
        adapter.setViewModel(chatViewModel)
        chatViewModel.loadChatinfoList()
        chatViewModel.receiveMessage()
    }

    override fun memberClicked(client_ID: String) {
        val view = layoutInflater.inflate(R.layout.dialog_add_chatroom,null)
        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setView(view)
            .setNegativeButton("취소"){ dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("보내기"){dialog, _ ->
                viewModel.createChatRoom(client_ID, view.findViewById<TextInputEditText>(R.id.tiet_chatroom_name).text.toString())
                dialog.dismiss()
            }
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
        }
        dialog.show()
    }
}
