package com.example.myapplication.view.chat

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.view.base.BaseViewModel
import java.util.*

class ChatViewModel: BaseViewModel {

    val TAG = ChatViewModel::class.java.simpleName
    val isLoading:ObservableField<Boolean> = ObservableField()
    val chatInfoList:ObservableArrayList<ChatInfo> = ObservableArrayList()

    init{
        isLoading.set(true)
        chatInfoList.add(ChatInfo("1","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("2","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("3","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("4","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("5","2","chat1",Calendar.getInstance().time,"ffff"))
    }

    constructor(dataManager: DataSource):super(dataManager)

    fun sendMessage(chatInfo: ChatInfo){
        getCompositeDisposable().add(getDataManager().sendMessage(chatInfo)
            .subscribe { result ->
                Log.e(TAG,result.string())
            })
    }

    fun loadChatinfoList(roomId:String="chat1"){
        getCompositeDisposable().add(getDataManager().loadChatInfoList(roomId)
            .subscribe({ result->
                isLoading.set(false)
                chatInfoList.addAll(result)
            },{
                Log.e(TAG,it.message)
            }))
    }

    fun sendMessage(message:String){
        getCompositeDisposable().add(getDataManager().getUser()
            .subscribe({ user->
              ChatInfo("1",user.id,"chat1", Calendar.getInstance().time,message)
                  .let{
                      sendMessage(it)
                  }
            },{
                Log.e(TAG,it.message)
            }))
    }

    fun receiveMessage(){
        getCompositeDisposable().add(getDataManager().receiveMessage()
            .subscribe({
                chatInfoList.add(it)
            },{
               Log.e(TAG,it.message)
            }))
    }
}