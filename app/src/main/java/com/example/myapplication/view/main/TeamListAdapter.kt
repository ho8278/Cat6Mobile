package com.example.myapplication.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Team
import com.example.myapplication.databinding.ItemTeamBinding

class TeamListAdapter(val listener:GroupChangeListener):RecyclerView.Adapter<TeamListAdapter.TeamViewHolder>(){

    val groupList=ArrayList<Team>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding=DataBindingUtil.inflate<ItemTeamBinding>(LayoutInflater.from(parent.context), R.layout.item_team,parent,false)
        return TeamViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class TeamViewHolder(val binding: ItemTeamBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position:Int){
            binding.viewmodel=TeamViewModel(AppInitialize.dataSource,groupList[position])
        }
    }
}