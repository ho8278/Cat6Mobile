package com.example.myapplication.view.chat

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.view.base.BaseViewModel
import io.reactivex.Single
import java.util.*

class ChatViewModel(dataManager: DataSource) : BaseViewModel(dataManager) {

    val TAG = ChatViewModel::class.java.simpleName
    val isLoading: ObservableField<Boolean> = ObservableField()
    val chatInfoList: ObservableArrayList<ChatInfo> = ObservableArrayList()
    val isNotice = ObservableBoolean()
    val notice = ObservableField<String>()
    val isToolbox = ObservableBoolean()
    val chatName = ObservableField<String>()
    val isSending = ObservableBoolean()
    lateinit var chatRoom: ChatRoom  //TODO: 나중에 로그인 후 파라미터로 전달

    init {
        isLoading.set(true)
        isNotice.set(false)
        isToolbox.set(false)
        isSending.set(true)
        getCompositeDisposable().add(
            getDataManager().loadChatRoom()
                .subscribe({ list ->
                    chatRoom = list[0]
                    chatName.set(list[0].name)
                })
        )
    }

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
        isSending.set(false)
        val userID = getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
        val chatInfoID = UUID.randomUUID().toString()
        getDataManager().saveItem(PreferenceHelperImpl.RECENT_CHATINFO_ID, chatInfoID)
        val chatInfo = ChatInfo(chatInfoID, userID, chatRoom.id, Calendar.getInstance().time, message)
        getCompositeDisposable().add(
            getDataManager().sendMessage(chatInfo)
                .subscribe({
                    isSending.set(true)
                    chatInfoList.add(chatInfo)
                    Log.e(TAG, it.string())
                }, {
                    isSending.set(true)
                    Log.e(TAG, it.message)
                })
        )
    }

    fun receiveMessage() {
        getCompositeDisposable().add(
            getDataManager().receiveMessage()
                .subscribe({
                    chatInfoList.add(it)
                    Log.e(TAG, it.id)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun showToolBox() {
        if (isToolbox.get())
            isToolbox.set(false)
        else
            isToolbox.set(true)
    }
}