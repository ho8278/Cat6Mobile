package com.example.myapplication.view.main

import com.example.myapplication.data.model.ChatRoom

interface MainNavigator{
    fun setChatViewModel(chatRoom:ChatRoom?)
}