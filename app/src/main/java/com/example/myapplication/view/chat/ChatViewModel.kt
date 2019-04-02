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

    val isLoading:ObservableField<Boolean> = ObservableField()
    val chatInfoList:ObservableArrayList<ChatInfo> = ObservableArrayList()

    init{
        isLoading.set(true)
    }

    constructor(dataManager: DataSource):super(dataManager)

    fun getChatRoomMessage(chatroomId:String="chat1"){
    }

    fun sendMessage(chatInfo: ChatInfo){
        getCompositeDisposable().add(getDataManager().sendMessage(chatInfo)
            .subscribe { result ->
                Log.e("fff",result.string())
            })
    }

    fun sendMessage(message:String){
        getCompositeDisposable().add(getDataManager().getUser()
            .subscribe({ user->
              ChatInfo("1",user.id,"chat1", Calendar.getInstance().time,message)
                  .let{
                      sendMessage(it)
                  }
            },{
                Log.e("aaa",it.message)
            }))
    }

    fun receiveMessage(chatInfo: ChatInfo){
        getCompositeDisposable().add(getDataManager().receiveMessage()
            .subscribe({
                chatInfoList.add(it)
            },{
               Log.e("error",it.message)
            }))
    }
}