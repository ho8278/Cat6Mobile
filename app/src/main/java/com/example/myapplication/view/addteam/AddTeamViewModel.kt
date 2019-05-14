package com.example.myapplication.view.addteam

import android.util.Log
import com.example.myapplication.data.DataSource
import com.example.myapplication.view.base.BaseViewModel

class AddTeamViewModel:BaseViewModel{
    val TAG=AddTeamViewModel::class.java.simpleName
    constructor(dataSource: DataSource):super(dataSource)

    fun createTeam(teamName:String){
        getCompositeDisposable().add(
            getDataManager().createTeam(teamName)
                .subscribe({it->
                    Log.e(TAG,it.toString())
                },{
                    Log.e(TAG,it.message)
                })
        )
    }
}