package com.example.myapplication.view.main

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.Team
import com.example.myapplication.databinding.ItemAddGroupBinding
import com.example.myapplication.databinding.ItemTeamBinding
import com.example.myapplication.view.addteam.AddTeamActivity
import com.example.myapplication.view.addusertoteam.AddUserToTeamActivity
import com.example.myapplication.view.base.BaseViewHolder

class TeamListAdapter(val listener: GroupChangeListener) : RecyclerView.Adapter<BaseViewHolder>() {

    private val ADD_VIEWHOLDER = 1
    private val TEAM_VIEWHOLDER = 2
    private var isCurrent = false

    val groupList = MutableList(1){Team("dummy","dummy")}

    fun setList(list: MutableList<Team>) {
        groupList.clear()
        groupList.addAll(list)
        groupList.add(Team("dummy", "dummy"))
        notifyItemRangeInserted(0,list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == ADD_VIEWHOLDER) {
            val binding = DataBindingUtil.inflate<ItemAddGroupBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_add_group,
                parent,
                false
            )
            return AddTeamViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemTeamBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_team,
                parent,
                false
            )
            return TeamViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == groupList.lastIndex)
            return ADD_VIEWHOLDER
        else
            return TEAM_VIEWHOLDER
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class TeamViewHolder(val binding: ItemTeamBinding) : BaseViewHolder(binding) {

        override fun bind(position: Int) {
            binding.viewmodel = TeamViewModel(AppInitialize.dataSource, groupList[position])
            binding.llContainer.setOnClickListener {
                listener.groupChanged(groupList[position].id)
            }
            binding.ivAddUser.setOnClickListener {
                val intent = Intent(it.context,AddUserToTeamActivity::class.java)
                it.context.startActivity(intent)
            }
        }
    }

    inner class AddTeamViewHolder(val binding: ItemAddGroupBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.llContainer.setOnClickListener {
                val intent=Intent(binding.llContainer.context,AddTeamActivity::class.java)
                binding.llContainer.context.startActivity(intent)
            }
        }
    }
}