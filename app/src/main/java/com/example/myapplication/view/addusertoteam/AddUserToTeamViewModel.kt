package com.example.myapplication.view.addusertoteam

import android.util.Log
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.User
import com.example.myapplication.view.addschedule.AddNavigator
import com.example.myapplication.view.base.BaseViewModel
import com.example.myapplication.view.main.ErrorCode

class AddUserToTeamViewModel(dataSource: DataSource, val listener:AddNavigator):BaseViewModel(dataSource){

    val TAG = AddUserToTeamViewModel::class.java.simpleName
    lateinit var findUser:User
    val nickname = ObservableField<String>()
    val searchText = ObservableField<String>()

    fun searchUser(){
        getCompositeDisposable().add(
            getDataManager().loadUser(searchText.get()?:"")
                .subscribe({
                    if(it.responseCode.toInt() == ErrorCode.SUCCESS.code){
                        val user = it.data
                        findUser=user
                        nickname.set("이름: ${findUser.name}   닉네임: ${findUser.nickname}")
                    }else{
                        nickname.set("유저 정보가 없습니다.")
                    }
                },{
                    Log.e(TAG, it.message)
                })
        )
    }

    fun addUserToTeam(){
        if(!this::findUser.isInitialized){
            listener.OnSaveFail(ErrorCode.NOT_SELECTED)
            return
        }

        getCompositeDisposable().add(
            getDataManager().addUserToTeam(findUser.id)
                .subscribe({
                    if(it==ErrorCode.SUCCESS)
                        listener.OnSaveSuccess()
                    else
                        listener.OnSaveFail(it)
                },{
                    Log.e(TAG, it.message)
                })
        )
    }
}