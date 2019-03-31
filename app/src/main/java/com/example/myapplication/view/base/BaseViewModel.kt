package com.example.myapplication.view.base

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(private val dataManager: DataSource) {

    private val disposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    fun onDestroy(){
        disposable.dispose()
    }

    fun getCompositeDisposable(): CompositeDisposable = disposable

    fun getDataManager(): DataSource = dataManager
}