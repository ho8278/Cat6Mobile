/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.view.base.BaseViewModel


class ReferenceListViewModel(val dataSource : DataSource) : BaseViewModel(dataSource) {

    val referenceList : ObservableArrayList<Reference> = ObservableArrayList()

    fun init() {
        val teamID = getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        //getCompositeDisposable().add(getDataManager())

    }
}