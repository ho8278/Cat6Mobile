package com.example.myapplication.view.main

import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Team
import com.example.myapplication.view.base.BaseViewModel

class TeamViewModel:BaseViewModel{
    val teamName=ObservableField<String>()
    constructor(dataSource: DataSource, team:Team):super(dataSource){
        teamName.set(team.name)
    }
}