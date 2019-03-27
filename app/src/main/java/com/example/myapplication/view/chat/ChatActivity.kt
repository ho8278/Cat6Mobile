package com.example.myapplication.view.chat

import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityChatBinding
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
    }
}
