package com.example.myapplication.view.detailschedule

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.LayoutDetailScheduleBinding
import com.example.myapplication.view.addschedule.AddScheduleActivity
import com.example.myapplication.view.calendar.CalendarViewModel
import org.joda.time.DateTime

class CustomDialog(
    context: Context,
    val list: MutableList<Schedule>,
    val listener: ScheduleChangeListener?,
    style: Int,
    val selectTime: DateTime,
    val viewModel: CalendarViewModel
) :
    Dialog(context, style), ScheduleListViewPagerAdapter.OnFabClickListener, DetailScheduleListAdapter.OnScheduleItemClick {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView() {
        val binding = DataBindingUtil.inflate<LayoutDetailScheduleBinding>(
            layoutInflater,
            R.layout.layout_detail_schedule,
            null,
            false
        )
        binding.vpSchedule.apply {
            pageMargin = 30
            adapter = ScheduleListViewPagerAdapter(this@CustomDialog, this@CustomDialog, selectTime, viewModel)
            currentItem = 150
            setPageTransformer(
                false,
                ViewPagerAnimator(0.8f)
            )
        }

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.7f
        }
        window.attributes = layoutParams

        setContentView(binding.root)

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawableResource(R.color.colorClear)
    }

    override fun onItemClick(schedule: Schedule) {
        val intent = Intent(context, AddScheduleActivity::class.java)
        intent.putExtra("SELECT_ITEM",schedule)
        context.startActivity(intent)
    }

    override fun onClick(dateTime: DateTime) {
        val intent = Intent(context, AddScheduleActivity::class.java)
        intent.putExtra("SELECT_DAY",selectTime)
        context.startActivity(intent)
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