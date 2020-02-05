package com.example.myapplication.view.detailschedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.LayoutDetailScheduleBinding
import com.example.myapplication.view.addschedule.AddScheduleActivity
import com.example.myapplication.view.base.BaseFragment
import com.example.myapplication.view.calendar.CalendarViewModel
import org.joda.time.DateTime

class DetailScheduleFragment(val viewmodel: CalendarViewModel, var time: DateTime) :
    BaseFragment<LayoutDetailScheduleBinding, CalendarViewModel>(), DetailScheduleListAdapter.OnScheduleItemClick,
    ScheduleListViewPagerAdapter.OnFabClickListener {
    override val TAG: String
        get() = DetailScheduleFragment::class.java.simpleName
    companion object{
        val DATA_CHANGE = 3000
    }
    override fun getViewModel(dataManager: DataSource): CalendarViewModel {
        return viewmodel
    }

    override fun getLayoutId(): Int = R.layout.layout_detail_schedule

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        isViewModelFinish = false
    }

    override fun onItemClick(schedule: Schedule) {
        val intent = Intent(context, AddScheduleActivity::class.java)
        intent.putExtra("SELECT_ITEM", schedule)
        activity?.startActivityForResult(intent, DATA_CHANGE)
    }

    override fun onClick(dateTime: DateTime) {
        val intent = Intent(context, AddScheduleActivity::class.java)
        intent.putExtra("SELECT_DAY", time)
        activity?.startActivityForResult(intent, DATA_CHANGE)
    }

    private fun initView() {
        binding.vpSchedule.apply {
            pageMargin = 30
            adapter =
                ScheduleListViewPagerAdapter(time, viewmodel, this@DetailScheduleFragment, this@DetailScheduleFragment)
            currentItem = 150
            setPageTransformer(
                false,
                ViewPagerAnimator(0.8f)
            )
            addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    time = time.plusDays(position-301/2)
                }
            })
        }
    }

    class ViewPagerAnimator(val smallerScale: Float) : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val absPosition = Math.abs(position)
            if (absPosition >= 1) {
                page.scaleY = smallerScale
            } else {
                page.scaleY = (smallerScale - 1) * absPosition + 1
            }
        }
    }
}