package com.example.myapplication.view.mypage

import android.util.Log
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.view.base.BaseViewModel

class MyPageViewModel(dataSource: DataSource):BaseViewModel(dataSource){
    val id = ObservableField<String>()
    val name = ObservableField<String>()
    val nickname = ObservableField<String>()
    val TAG = MyPageViewModel::class.java.simpleName

    fun init(){
        getCompositeDisposable().add(
            getDataManager().getCurrentUser()
                .subscribe({
                    id.set(it.id)
                    name.set(it.name)
                    nickname.set(it.nickname)
                },{
                  Log.e(TAG,it.message)
                })
        )
    }
}