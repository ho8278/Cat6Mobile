package com.example.myapplication.view.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.remote.api.ApiHelperImpl
import com.example.myapplication.databinding.ActivityChatBinding
import com.example.myapplication.view.base.BaseActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChatActivity : BaseActivity<ActivityChatBinding, ChatViewModel>() {

    override val TAG: String
        get() = ChatActivity::class.java.simpleName

    override fun getViewModel(dataSource: DataSource): ChatViewModel {
        return ChatViewModel(dataSource)
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_chat
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarChatinfo)
        binding.viewmodel = viewModel
        viewModel.loadChatinfoList()    //나중에 채팅방 ID 가져와야함
        viewModel.receiveMessage()
        binding.buttonSend.setOnClickListener {
            viewModel.sendButtonClicked(binding.etMessagebox.text.toString())
            binding.etMessagebox.text?.clear()
        }

        binding.rvChat.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvChat.adapter = ChatFragmentAdapter(applicationContext)
    }
}
