package com.example.myapplication.view.base

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(private val dataManager: DataSource):ViewModel(){

    private val disposable:CompositeDisposable by lazy{
        CompositeDisposable()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun getCompositeDisposable():CompositeDisposable{
        return disposable
    }

    fun getDataManager():DataSource{
        return dataManager
    }
}