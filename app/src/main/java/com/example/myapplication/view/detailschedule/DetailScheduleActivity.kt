package com.example.myapplication.view.detailschedule

import android.os.Bundle
import android.util.Log
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ActivityDetailScheduleBinding
import com.example.myapplication.view.base.BaseActivity

class DetailScheduleActivity:BaseActivity<ActivityDetailScheduleBinding,DetailScheduleViewModel>(){

    private val SCHEDULE_INFO="SCHEDULE_INFO"

    override val TAG: String
        get() = DetailScheduleActivity::class.java.simpleName
    override fun getLayoutID(): Int {
        return R.layout.activity_detail_schedule
    }

    override fun getViewModel(dataSource: DataSource): DetailScheduleViewModel {
        return DetailScheduleViewModel(dataSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val intentData=intent.extras[SCHEDULE_INFO] as Schedule
        Log.e(TAG,intentData.toString())*/
    }
}