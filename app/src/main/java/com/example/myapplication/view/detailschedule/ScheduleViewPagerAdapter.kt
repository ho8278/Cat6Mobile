package com.example.myapplication.view.detailschedule

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ItemDetailScheduleBinding
import com.example.myapplication.view.addschedule.AddScheduleViewModel

class ScheduleViewPagerAdapter(val list: MutableList<Schedule>, val listener: ScheduleChangeListener) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = DataBindingUtil.inflate<ItemDetailScheduleBinding>(
            LayoutInflater.from(container.context),
            R.layout.item_detail_schedule,
            container,
            false
        )
        val viewModel =
            AddScheduleViewModel(DataManager.getInstance(container.context.applicationContext), null, list[position])
        binding.viewmodel = viewModel
        binding.apply {
            tvDelete.setOnClickListener {
                val dialog= AlertDialog.Builder(container.context)
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->
                        viewModel.delteSchedule()
                        listener.OnDelete(position)
                        list.removeAt(position)
                        notifyDataSetChanged()
                    }
                    .setNegativeButton("취소"){ dialog, _ ->
                        dialog.cancel()
                    }
            }
            tvUpdate.setOnClickListener {
                viewModel.updateSchedule()
            }
        }
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return list.size
    }

    fun setList(scheduleList: MutableList<Schedule>) {
        list.clear()
        list.addAll(scheduleList)
        notifyDataSetChanged()
    }
}