package com.example.myapplication.view.detailschedule

import android.os.Bundle
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.LayoutDetailScheduleBinding
import com.example.myapplication.view.base.BaseFragment
import com.example.myapplication.view.calendar.CalendarViewModel
import org.joda.time.DateTime

class DetailScheduleFragment(val viewmodel:CalendarViewModel, val time:DateTime):BaseFragment<LayoutDetailScheduleBinding, CalendarViewModel>(){
    override val TAG: String
        get() = DetailScheduleFragment::class.java.simpleName

    override fun getViewModel(dataManager: DataSource): CalendarViewModel {
        return viewmodel
    }

    override fun getLayoutId(): Int = R.layout.layout_detail_schedule

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }


    private fun initView() {
        binding.vpSchedule.apply {
            pageMargin = 30
            adapter = ScheduleListViewPagerAdapter(time)
            currentItem = 150
            setPageTransformer(
                false,
                CustomDialog.ViewPagerAnimator(0.8f)
            )
        }
    }
}