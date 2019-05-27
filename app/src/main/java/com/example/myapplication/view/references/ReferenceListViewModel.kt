/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ServerResponse
import com.example.myapplication.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ReferenceListViewModel(val dataSource : DataSource) : BaseViewModel(dataSource) {

    val referenceList : ObservableArrayList<Reference> = ObservableArrayList()
    private val originList = mutableListOf<Reference>()
    private lateinit var preSearchDisposable : Disposable

    fun init() {
        val teamID = getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        //getCompositeDisposable().add(getDataManager())

    }

    fun loadReferenceList(id : String)
        = dataSource.loadReferences(id)
        .subscribe {
            setReferences(it.data)
        }

    fun addReference(reference: Reference) {
        originList.add(reference)
        referenceList.clear()
        referenceList.addAll(originList)
    }

    private fun setReferences(reference: List<Reference>) {
        originList.clear()
        originList.addAll(reference)
        referenceList.clear()
        referenceList.addAll(originList)
    }

    fun filterItem(str: String) {
        if (::preSearchDisposable.isInitialized && !preSearchDisposable.isDisposed)
            preSearchDisposable.dispose()

        preSearchDisposable = Observable.fromIterable(originList)
            .filter { t -> t.title.contains(str) }
            .toList()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("Test", it.size.toString())
                referenceList.clear()
                if (str.isEmpty()) {
                    referenceList.addAll(originList)
                } else {
                    referenceList.addAll(ArrayList(it))
                }
            }, {
                it.stackTrace[0]
            })
    }
}