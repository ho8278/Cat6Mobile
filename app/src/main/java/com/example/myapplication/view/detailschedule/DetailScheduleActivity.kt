package com.example.myapplication.view.detailschedule

import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.LayoutDetailScheduleBinding
import com.example.myapplication.view.base.BaseActivity

class DetailScheduleActivity:BaseActivity<LayoutDetailScheduleBinding,DetailScheduleViewModel>(){

    override val TAG: String
        get() = DetailScheduleActivity::class.java.simpleName
    override fun getLayoutID(): Int {
        return R.layout.layout_detail_schedule
    }

    override fun getViewModel(dataSource: DataSource): DetailScheduleViewModel {
        return DetailScheduleViewModel(dataSource, mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}