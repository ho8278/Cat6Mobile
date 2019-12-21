package com.example.myapplication.view.addfriends

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogAddFriendsBinding
import com.example.myapplication.view.addschedule.AddNavigator
import com.example.myapplication.view.main.AppInitialize
import com.example.myapplication.view.main.ErrorCode
import kotlinx.android.synthetic.main.dialog_add_friends.*

class AddFriendsDialog : Dialog, AddNavigator,AddUserChangeListener {
    constructor(context: Context) : super(context)

    lateinit var binding: DialogAddFriendsBinding
    lateinit var viewModel:AddFriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_add_friends, null, false)
        setContentView(binding.root)

        viewModel = AddFriendViewModel(AppInitialize.dataSource,this)
        binding.viewmodel=viewModel

        binding.rvFriends.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvFriends.adapter = AddFriendAdapter(this)

        setCancelable(true)
        tv_cancel.setOnClickListener {
            dismiss()
        }


        tv_ok.setOnClickListener {
            val adapter = binding.rvFriends.adapter as AddFriendAdapter
            viewModel.setUserList(adapter.addUserList)
            dismiss()
        }
    }

    override fun onChange(list: List<String>) {
        viewModel.addUserChange(list)
    }

    override fun OnSaveFail(errorCode: ErrorCode) {
        Toast.makeText(context,errorCode.description,Toast.LENGTH_SHORT).show()
    }

    override fun OnSaveSuccess() {
        Toast.makeText(context,"성공",Toast.LENGTH_SHORT).show()
        context.sendBroadcast(Intent("updateChatList"))
    }
}