package com.example.myapplication.view.chat

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityChatBinding
import com.example.myapplication.view.base.BaseFragment

class ChatFragment : BaseFragment<ActivityChatBinding, ChatViewModel>() {

    override val TAG: String
        get() = ChatFragment::class.java.simpleName

    override fun getViewModel(dataSource: DataSource): ChatViewModel {
        return ChatViewModel(dataSource)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_chat
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
