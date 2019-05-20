package com.example.myapplication.view.base

import com.example.myapplication.data.DataSource
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(private val dataManager: DataSource) {

    private val disposable = CompositeDisposable()
    fun onDestroy(){
        if(!disposable.isDisposed)
            disposable.dispose()
    }

    fun getCompositeDisposable(): CompositeDisposable = disposable

    fun getDataManager(): DataSource = dataManager
}