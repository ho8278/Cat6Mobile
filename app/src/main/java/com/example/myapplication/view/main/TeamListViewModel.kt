package com.example.myapplication.view.main

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.Team
import com.example.myapplication.view.base.BaseViewModel
import java.util.*

class TeamListViewModel:BaseViewModel{

    private val TAG=TeamListViewModel::class.java.simpleName
    constructor(dataSource: DataSource):super(dataSource)

    val teamList=ObservableArrayList<Team>()

    fun init(){
        val userID=getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_USER_ID)
        getCompositeDisposable().add(
            getDataManager().loadTeam(userID)
                .subscribe({ list->
                    teamList.addAll(list)
                    Log.e(TAG,list.toString())
                },{
                    Log.e(TAG,it.toString())
                })
        )
    }
}