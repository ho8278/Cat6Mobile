package com.example.myapplication.view.main

import android.Manifest
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.MockDataManager
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.util.ChatSocketService
import com.example.myapplication.util.FilePathProvider
import com.example.myapplication.view.addfriends.AddFriendsDialog
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.calendar.CalendarActivity
import com.example.myapplication.view.chat.ChatInfoListAdapter
import com.example.myapplication.view.chat.ChatViewModel
import com.example.myapplication.view.mypage.MyPageActivity
import com.example.myapplication.view.references.ReferencesActivity
import com.example.myapplication.view.vote.VoteActivity
import com.example.myapplication.view.web.WebActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), GroupChangeListener, ChatRoomChangeListener, MemberClickListener,
    MainNavigator, MainViewRefresh {
    override val TAG: String
        get() = MainActivity::class.java.simpleName
    private val FILE_REQUEST_CODE = 10
    private lateinit var fragment: TeamListFragment
    private lateinit var chatViewModel: ChatViewModel
    var receiver:BroadcastReceiver = object:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.updateUI()
        }
    }

    var serviceIntent:Intent? = null
    var isRotation = true

    override fun getViewModel(dataSource: DataSource): MainViewModel {
        return MainViewModel(dataSource, this)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMenu()

        initView()

        includeInit()

        if(AppInitialize.dataSource is DataManager){
            if(ChatSocketService.serviceIntent==null){
                serviceIntent = Intent(this,ChatSocketService::class.java)
                startService(serviceIntent)
            }else{
                serviceIntent = ChatSocketService.serviceIntent
            }
        }

        fragment = TeamListFragment()

        iv_show_group.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.apply {
                replace(R.id.fragment_show_team, fragment)
                commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(AppInitialize.dataSource is DataManager){
            registerReceiver(receiver, IntentFilter("updateChatList"))
        }
    }

    override fun onPause() {
        super.onPause()
        if(AppInitialize.dataSource is DataManager){
            unregisterReceiver(receiver)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(AppInitialize.dataSource is DataManager){
            if(serviceIntent!=null){
                stopService(serviceIntent)
                serviceIntent=null
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
        binding.rcvMainParticipants.run {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = MemberListAdapter(this@MainActivity)
            itemAnimator = FadeInDownAnimator()
        }

        binding.rvChat.run{
            adapter = ChatListAdapter(this@MainActivity)
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            itemAnimator = FadeInDownAnimator()
        }
        viewModel.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    val uri = data?.data
                    Log.e(TAG, uri.toString())
                    if (uri != null) {
                        val str = FilePathProvider.getRealPathFromURI(applicationContext,uri)
                        chatViewModel.sendFile(str ?: "")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun includeInit() {
        include_chat.apply {
            button_send.setOnClickListener {
                chatViewModel.sendButtonClicked(et_messagebox.text.toString())
                et_messagebox.text?.clear()
            }

            iv_show_toolbox.setOnClickListener {
                if(isRotation){
                    it.animate().rotation(45f)
                    isRotation = false
                }else{
                    it.animate().rotation(0f)
                    isRotation = true
                }
                chatViewModel.showToolBox()
            }

            iv_document.setOnClickListener {
                TedPermission.with(applicationContext)
                    .setPermissionListener(object:PermissionListener{
                        override fun onPermissionGranted() {
                            val fileChooser = Intent(Intent.ACTION_GET_CONTENT)
                            fileChooser.setType("*/*")
                            fileChooser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivityForResult(Intent.createChooser(fileChooser, "Open"), FILE_REQUEST_CODE)
                        }

                        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        }
                    })
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
            }

            iv_notice.setOnClickListener {
                chatViewModel.showNotice()
            }

            iv_vote.setOnClickListener {
                if(AppInitialize.dataSource is MockDataManager){
                    Toast.makeText(this@MainActivity,"서버상의 문제로 현재는 실행 할 수 없습니다. 죄송합니다 ",Toast.LENGTH_LONG).show()
                }else{
                    val intent = Intent(context,VoteActivity::class.java)
                    startActivity(intent)
                }
            }

            iv_add_friend.setOnClickListener {
                val dialog = AddFriendsDialog(context)
                dialog.show()

                val display = windowManager.defaultDisplay
                val point = Point()
                display.getSize(point)

                val window = dialog.window
                val x = point.x*0.8f
                val y = point.y*0.7f

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

        btn_main_schedule.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        btn_main_references.setOnClickListener {
            if(AppInitialize.dataSource is MockDataManager){
                Toast.makeText(this,"서버상의 문제로 현재는 실행 할 수 없습니다. 죄송합니다 ",Toast.LENGTH_LONG).show()
            }
            else {
                startActivity(Intent(this, ReferencesActivity::class.java))
            }
        }

        btn_main_conference.setOnClickListener {
            if(AppInitialize.dataSource is MockDataManager){
                Toast.makeText(this,"서버상의 문제로 현재는 실행 할 수 없습니다. 죄송합니다 ",Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this,WebActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun groupChanged(groupID: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.commit()
        viewModel.changeTeam(groupID)
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
        if(AppInitialize.dataSource is MockDataManager){
            Toast.makeText(this,"현재 리팩토링 진행중입니다.", Toast.LENGTH_LONG).show()
            return
        }
        val view = layoutInflater.inflate(R.layout.dialog_add_chatroom,null)
        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setView(view)
            .setNegativeButton("취소"){ dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("보내기") { dialog, _ ->
                viewModel.createChatRoom(
                    client_ID,
                    view.findViewById<TextInputEditText>(R.id.tiet_chatroom_name).text.toString()
                )
                dialog.dismiss()
            }
            .create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
        }
        dialog.show()
    }

    override fun refresh() {
        viewModel.updateUI()
    }
}
