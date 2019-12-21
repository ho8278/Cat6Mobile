package com.example.myapplication.view.chat

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.MockDataManager
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.databinding.*
import com.example.myapplication.view.base.BaseViewHolder
import com.example.myapplication.view.main.AppInitialize
import java.text.SimpleDateFormat
import java.util.*

class ChatInfoListAdapter(var chatViewModel: ChatViewModel) :
    ListAdapter<ChatInfo, BaseViewHolder>(object : DiffUtil.ItemCallback<ChatInfo>() {
        override fun areItemsTheSame(oldItem: ChatInfo, newItem: ChatInfo): Boolean =
            oldItem.chatinfo_id == newItem.chatinfo_id

        override fun areContentsTheSame(oldItem: ChatInfo, newItem: ChatInfo): Boolean =
            oldItem.chatinfo_id == newItem.chatinfo_id
    }) {

    private val TAG = ChatInfoListAdapter::class.java.simpleName
    private var userId: String


    val MESSAGE_TEXT = 0
    val MESSAGE_FILE = 1
    val MESSAGE_PHOTO = 2

    val VIEW_TYPE_ME_TEXT = 111
    val VIEW_TYPE_YOU_TEXT = 112
    val VIEW_TYPE_ME_PHOTO = 113
    val VIEW_TYPE_YOU_PHOTO = 114
    val VIEW_TYPE_ME_FILE = 115
    val VIEW_TYPE_YOU_FILE = 116

    init {
        userId = AppInitialize.dataSource.getItem(PreferenceHelperImpl.CURRENT_USER_ID)
    }

    fun setList(changeList: MutableList<ChatInfo>) {
        submitList(changeList.toMutableList())
    }

    fun setViewModel(viewModel: ChatViewModel) {
        chatViewModel = viewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == VIEW_TYPE_ME_TEXT) {
            return DataBindingUtil.inflate<ItemMychatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_mychat,
                parent,
                false
            ).let {
                MeViewHolder(it)
            }
        }else if(viewType == VIEW_TYPE_ME_PHOTO){
            return DataBindingUtil.inflate<ItemMyPhotoChatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_my_photo_chat,
                parent,
                false
            ).let {
                MePhotoViewHolder(it)
            }
        } else if(viewType == VIEW_TYPE_ME_FILE) {
            return DataBindingUtil.inflate<ItemMyFileChatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_my_file_chat,
                parent,
                false
            ).let {
                MeFileViewHolder(it)
            }
        } else if(viewType == VIEW_TYPE_YOU_TEXT) {
            return DataBindingUtil.inflate<ItemTheirchatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_theirchat,
                parent,
                false
            ).let {
                YouViewHolder(it)
            }
        } else if(viewType == VIEW_TYPE_YOU_PHOTO) {
            return DataBindingUtil.inflate<ItemTheirPhotoChatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_their_photo_chat,
                parent,
                false
            ).let {
                YouPhotoViewHolder(it)
            }
        } else {
            return DataBindingUtil.inflate<ItemTheirFileChatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_their_file_chat,
                parent,
                false
            ).let {
                YouFileViewHolder(it)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        when(userId){
            getItem(position).send_user_id ->{
                when(getItem(position).type){
                    MESSAGE_TEXT->return VIEW_TYPE_ME_TEXT
                    MESSAGE_PHOTO->return VIEW_TYPE_ME_PHOTO
                    MESSAGE_FILE->return VIEW_TYPE_ME_FILE
                    else->return 0
                }
            }
            else->{
                when(getItem(position).type){
                    MESSAGE_TEXT->return VIEW_TYPE_YOU_TEXT
                    MESSAGE_PHOTO->return VIEW_TYPE_YOU_PHOTO
                    MESSAGE_FILE->return VIEW_TYPE_YOU_FILE
                    else->return 0
                }
            }
        }
    }

    inner class MeFileViewHolder(val binding:ItemMyFileChatBinding):BaseViewHolder(binding) {
        override fun bind(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.time = getItem(position).send_date
            val date: String = when (calendar.get(Calendar.AM_PM)) {
                Calendar.AM -> {
                    val dateFormat = SimpleDateFormat("오전 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                Calendar.PM -> {
                    val dateFormat = SimpleDateFormat("오후 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                else -> ""
            }
            binding.tvMessageClock.setText(date)
            binding.tvFilename.setText(getItem(position).message)
        }
    }

    inner class YouFileViewHolder(val binding:ItemTheirFileChatBinding):BaseViewHolder(binding) {
        override fun bind(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.time = getItem(position).send_date
            val date: String = when (calendar.get(Calendar.AM_PM)) {
                Calendar.AM -> {
                    val dateFormat = SimpleDateFormat("오전 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                Calendar.PM -> {
                    val dateFormat = SimpleDateFormat("오후 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                else -> ""
            }
            AppInitialize.dataSource.getUser(getItem(position).send_user_id)
                .subscribe({
                    Log.e(TAG, "${it.nickname}")
                    binding.tvTheirname.setText(it.nickname)
                    binding.tvFilename.setText(getItem(position).message)
                    binding.tvMessageClock.setText(date)
                }, {
                    Log.e(TAG, it.message)
                })
        }
    }

    inner class MePhotoViewHolder(val binding:ItemMyPhotoChatBinding):BaseViewHolder(binding) {
        override fun bind(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.time = getItem(position).send_date
            val date: String = when (calendar.get(Calendar.AM_PM)) {
                Calendar.AM -> {
                    val dateFormat = SimpleDateFormat("오전 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                Calendar.PM -> {
                    val dateFormat = SimpleDateFormat("오후 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                else -> ""
            }
            binding.tvMessageClock.setText(date)
            if(AppInitialize.dataSource is MockDataManager){
                Glide.with(binding.root.context)
                    .load(BitmapFactory.decodeFile(getItem(position).message))
                    .override(500,500)
                    .into(binding.ivPhoto)
            }else{

            }
        }
    }

    inner class YouPhotoViewHolder(val binding:ItemTheirPhotoChatBinding):BaseViewHolder(binding) {
        override fun bind(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.time = getItem(position).send_date
            val date: String = when (calendar.get(Calendar.AM_PM)) {
                Calendar.AM -> {
                    val dateFormat = SimpleDateFormat("오전 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                Calendar.PM -> {
                    val dateFormat = SimpleDateFormat("오후 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                else -> ""
            }
            if(AppInitialize.dataSource is MockDataManager){
            }else{
                Glide.with(binding.root.context)
                    .load(getItem(position).message)
                    .override(500,500)
                    .into(binding.ivPhoto)

                AppInitialize.dataSource.getUser(getItem(position).send_user_id)
                    .subscribe({
                        Log.e(TAG, "${it.nickname}")
                        binding.tvTheirname.setText(it.nickname)
                        binding.tvMessageClock.setText(date)
                    }, {
                        Log.e(TAG, it.message)
                    })
            }
        }
    }

    inner class MeViewHolder(val binding: ItemMychatBinding) : BaseViewHolder(binding) {

        override fun bind(position: Int) {
            val calendar = Calendar.getInstance()
            calendar.time = getItem(position).send_date
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
                Calendar.PM -> {
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
            calendar.time = getItem(position).send_date

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
                Calendar.PM -> {
                    val dateFormat = SimpleDateFormat("오후 hh:mm", Locale.KOREA)
                    dateFormat.format(calendar.time)
                }
                else -> ""
            }
            AppInitialize.dataSource.getUser(getItem(position).send_user_id)
                .subscribe({
                    Log.e(TAG, "${it.nickname}")
                    binding.tvTheirname.setText(it.nickname)
                    binding.tvMessageBody.setText(getItem(position).message)
                    binding.tvMessageClock.setText(date)
                }, {
                    Log.e(TAG, it.message)
                })
        }
    }
}