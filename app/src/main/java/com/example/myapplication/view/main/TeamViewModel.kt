package com.example.myapplication.view.main

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.Team
import com.example.myapplication.view.base.BaseViewModel

class TeamViewModel:BaseViewModel{
    val teamName=ObservableField<String>()
    val isCurrentTeam = ObservableBoolean(false)
    constructor(dataSource: DataSource, team:Team):super(dataSource){
        teamName.set(team.name)
        val currentTeamID = getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        if(currentTeamID == team.id)
            isCurrentTeam.set(true)
    }
}