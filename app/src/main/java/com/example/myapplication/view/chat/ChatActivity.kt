package com.example.myapplication.view.chat

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityChatBinding
import com.example.myapplication.util.PreferenceUtil
import com.example.myapplication.view.base.BaseActivity

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

        binding.viewmodel=viewModel
        viewModel.loadChatinfoList()    //나중에 채팅방 ID 가져와야함
        viewModel.receiveMessage()
        binding.buttonSend.setOnClickListener {
            viewModel.sendMessage(binding.etMessagebox.text.toString())
        }

        binding.rvChat.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        PreferenceUtil.getUserId(applicationContext)
            .let {
                binding.rvChat.adapter = ChatFragmentAdapter(it)
            }
    }
}
