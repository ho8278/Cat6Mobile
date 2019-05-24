package com.example.myapplication.view.addfriends

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.User
import com.example.myapplication.databinding.DialogAddFriendsBinding
import com.example.myapplication.view.main.AppInitialize
import kotlinx.android.synthetic.main.dialog_add_friends.*

class AddFriendsDialog : Dialog {
    constructor(context: Context) : super(context)
    constructor(context: Context, resId: Int) : super(context, resId)

    lateinit var binding: DialogAddFriendsBinding
    lateinit var viewModel:AddFriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_add_friends, null, false)
        setContentView(binding.root)

        viewModel = AddFriendViewModel(AppInitialize.dataSource)
        binding.viewmodel=viewModel

        binding.rvFriends.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvFriends.adapter = AddFriendAdapter()

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
}