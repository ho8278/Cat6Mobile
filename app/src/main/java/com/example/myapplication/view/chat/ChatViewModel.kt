package com.example.myapplication.view.chat

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.view.base.BaseViewModel
import io.reactivex.Single
import java.util.*

class ChatViewModel : BaseViewModel {

    val TAG = ChatViewModel::class.java.simpleName
    val isLoading: ObservableField<Boolean> = ObservableField()
    val chatInfoList: ObservableArrayList<ChatInfo> = ObservableArrayList()

    init {
        isLoading.set(true)
    }

    constructor(dataManager: DataSource) : super(dataManager)

    fun loadChatinfoList(roomId: String = "chat1") {
        getCompositeDisposable().add(
            getDataManager().loadChatInfoList(roomId)
                .subscribe({ result ->
                    isLoading.set(false)
                    chatInfoList.addAll(result)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun sendButtonClicked(message: String) {
        getCompositeDisposable().add(getDataManager().getCurrentUser()
            .map { user -> ChatInfo(UUID.randomUUID().toString(), user.id, "chat1", Calendar.getInstance().time, message) }
            .flatMap { getDataManager().sendMessage(it) }
            .subscribe({
                Log.e(TAG,it.string())
            }, {
                Log.e(TAG, it.message)
            })
        )
    }

    fun receiveMessage() {
        getCompositeDisposable().add(
            getDataManager().receiveMessage()
                .subscribe({
                    chatInfoList.add(it)
                    Log.e(TAG,it.id)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }
}