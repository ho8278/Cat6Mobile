package com.example.myapplication.view.addfriends

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.User
import com.example.myapplication.databinding.ItemAddFriendsBinding
import com.example.myapplication.view.base.BaseViewHolder

class AddFriendAdapter(val listener:AddUserChangeListener) : RecyclerView.Adapter<BaseViewHolder>() {

    val userList = mutableListOf<User>()
    val addUserList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ItemAddFriendsBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.item_add_friends, parent, false)

        return AddFriendsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AddFriendsViewHolder(val binding: ItemAddFriendsBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.nickname=userList[position].nickname
            binding.llContainer.setOnClickListener {
                if(addUserList.filter { it.nickname == userList[position].nickname }.isEmpty()){
                    addUserList.add(userList[position])
                    binding.ivCheck.visibility = View.VISIBLE
                }
                else{
                    binding.ivCheck.visibility = View.GONE
                    addUserList.remove(userList[position])
                }
                listener.onChange(addUserList.map { it.nickname })
            }
            /*binding.cbAdd.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    addUserList.add(userList[position])
                }else{
                    addUserList.remove(userList[position])
                }
                Log.e("AddFriendAdapter",addUserList.toString())
            }*/
        }
    }
}