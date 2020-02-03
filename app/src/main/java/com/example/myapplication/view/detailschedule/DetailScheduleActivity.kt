package com.example.myapplication.view.detailschedule

import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.LayoutDetailScheduleBinding
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.calendar.CalendarViewModel
import org.joda.time.DateTime

class DetailScheduleActivity : BaseActivity<LayoutDetailScheduleBinding,CustomDialogViewModel>(){
    override val TAG: String
        get() = DetailScheduleActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.layout_detail_schedule
    }

    override fun getViewModel(dataSource: DataSource): CustomDialogViewModel {
        return CustomDialogViewModel(dataSource, mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

    }

    private fun initView() {
        val time = intent.getSerializableExtra("TEST") as DateTime


        binding.vpSchedule.apply {
            pageMargin = 30
            adapter = ScheduleListViewPagerAdapter(time)
            currentItem = 150
            setPageTransformer(
                false,
                CustomDialog.ViewPagerAnimator(0.8f)
            )
        }

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawableResource(R.color.colorClear)
    }
}