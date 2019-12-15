package com.example.myapplication.view.chat

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.view.base.BaseViewModel
import java.util.*

class ChatViewModel(dataManager: DataSource, val chatRoom: ChatRoom) : BaseViewModel(dataManager) {
    val MESSAGE_TEXT = 0
    val MESSAGE_FILE = 1
    val MESSAGE_PHOTO = 2
    val TAG = ChatViewModel::class.java.simpleName
    val isLoading: ObservableField<Boolean> = ObservableField()
    val chatInfoList: ObservableArrayList<ChatInfo> = ObservableArrayList()
    val isNotice = ObservableBoolean()
    val notice = ObservableField<String>()
    val isToolbox = ObservableBoolean()
    val chatName = ObservableField<String>()
    val isSending = ObservableBoolean()

    init {
        isLoading.set(true)
        isNotice.set(false)
        isToolbox.set(false)
        isSending.set(true)
    }

    fun loadChatinfoList() {
        if (chatRoom.id.isEmpty()) {
            chatName.set("새 대화 상대를 찾아보세요")
            getDataManager().saveItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID, "")
            isLoading.set(false)
            return
        }
        getDataManager().saveItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID, chatRoom.id)
        getCompositeDisposable().add(
            getDataManager().loadChatInfoList(chatRoom.id)
                .subscribe({ result ->
                    isLoading.set(false)
                    chatName.set(chatRoom.name)
                    chatInfoList.addAll(result)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun sendButtonClicked(message: String, messageType: Int = MESSAGE_TEXT) {
        val userID = getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
        val chatInfoID = UUID.randomUUID().toString()
        val chatInfo = ChatInfo(chatInfoID, userID, chatRoom.id, Calendar.getInstance().time, message, messageType)
        getCompositeDisposable().add(
            getDataManager().sendMessage(chatInfo)
                .subscribe({
                    chatInfoList.add(chatInfo)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun sendFile(path: String) {
        if (path.isEmpty())
            return
        getCompositeDisposable().add(
            getDataManager().uploadFile(path)
                .subscribe({ body ->
                    sendButtonClicked("")
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun receiveMessage() {
        getCompositeDisposable().add(
            getDataManager().receiveMessage()
                .subscribe({
                    if (it.chatroom_id == chatRoom.id)
                        chatInfoList.add(it)
                    Log.e(TAG, it.chatinfo_id)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }

    fun setNotice(text: String) {
        getCompositeDisposable().add(
            getDataManager().setNotice(text, chatRoom.id)
                .subscribe({ data ->
                    isNotice.set(true)
                    notice.set(text)
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

    fun showNotice() {
        if (isNotice.get()) {
            isNotice.set(false)
            return
        }
        getCompositeDisposable().add(
            getDataManager().loadNotice(chatRoom.id)
                .subscribe({ data ->
                    Log.e(TAG, data.toString())
                    isNotice.set(true)
                    notice.set(data.content)
                }, {
                    Log.e(TAG, it.message)
                })
        )
    }
}