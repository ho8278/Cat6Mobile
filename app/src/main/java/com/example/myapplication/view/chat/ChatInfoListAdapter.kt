package com.example.myapplication.view.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.R
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.databinding.ItemDatedividerBinding
import com.example.myapplication.databinding.ItemMychatBinding
import com.example.myapplication.databinding.ItemTheirchatBinding
import com.example.myapplication.view.base.BaseViewHolder
import com.example.myapplication.view.main.AppInitialize
import java.text.SimpleDateFormat
import java.util.*

class ChatInfoListAdapter(var chatViewModel: ChatViewModel) :
    ListAdapter<ChatInfo, BaseViewHolder>(object : DiffUtil.ItemCallback<ChatInfo>() {
        override fun areItemsTheSame(oldItem: ChatInfo, newItem: ChatInfo): Boolean {
            Log.e("DIFF", (oldItem == newItem).toString())
            return false
        }

        override fun areContentsTheSame(oldItem: ChatInfo, newItem: ChatInfo): Boolean {
            Log.e("DIFF", (oldItem == newItem).toString())
            return false
        }
    }) {

    private val TAG = ChatInfoListAdapter::class.java.simpleName
    private lateinit var userId: String

    val VIEW_TYPE_ME = 0
    val VIEW_TYPE_YOU = 1

    init {
        userId = AppInitialize.dataSource.getItem(PreferenceHelperImpl.CURRENT_USER_ID)
    }

    fun setList(changeList: MutableList<ChatInfo>) {
        submitList(changeList)
        notifyDataSetChanged()
    }

    fun setViewModel(viewModel:ChatViewModel){
        chatViewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == VIEW_TYPE_ME) {
            return DataBindingUtil.inflate<ItemMychatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_mychat,
                parent,
                false
            ).let {
                MeViewHolder(it)
            }
        } else {
            return DataBindingUtil.inflate<ItemTheirchatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_theirchat,
                parent,
                false
            ).let {
                YouViewHolder(it)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (userId == getItem(position).sendUserId) {
            return VIEW_TYPE_ME
        } else {
            return VIEW_TYPE_YOU
        }
    }

    inner class MeViewHolder(val binding: ItemMychatBinding) : BaseViewHolder(binding) {

        override fun bind(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.time = getItem(position).sendDate
            binding.mcvMessageContainer.setOnClickListener {
                val dialog = AlertDialog.Builder(binding.root.context)
                    .setMessage("공지사항으로 설정하시겠습니까?")
                    .setPositiveButton("확인") { dialog, _ ->
                        chatViewModel.setNotice(getItem(position).message)
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(true)
                    .create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                }
                dialog.show()
            }

            val date: String = when (calendar.get(Calendar.AM_PM)) {
                Calendar.AM -> {
                    val dateFormat = SimpleDateFormat("오전 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                Calendar.PM->{
                    val dateFormat = SimpleDateFormat("오후 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                else -> ""
            }
            binding.tvMessageBody.setText(getItem(position).message)
            binding.tvMessageClock.setText(date)
        }

    }

    inner class YouViewHolder(val binding: ItemTheirchatBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.time = getItem(position).sendDate

            binding.mcvMessageContainer.setOnClickListener {
                val dialog = AlertDialog.Builder(binding.root.context)
                    .setMessage("공지사항으로 설정하시겠습니까?")
                    .setPositiveButton("확인") { dialog, _ ->
                        chatViewModel.setNotice(getItem(position).message)
                        dialog.dismiss()
                    }
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(true)
                    .create()
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
                }
                dialog.show()
            }
            val date: String = when (calendar.get(Calendar.AM_PM)) {
                Calendar.AM -> {
                    val dateFormat = SimpleDateFormat("오전 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                Calendar.PM->{
                    val dateFormat = SimpleDateFormat("오후 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                else -> ""
            }
            binding.tvMessageBody.setText(getItem(position).message)
            binding.tvMessageClock.setText(date)
        }
    }

    inner class DivideViewHolder(val binding: ItemDatedividerBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}