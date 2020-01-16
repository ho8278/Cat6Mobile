package com.example.myapplication.view.addschedule

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ActivityAddShceduleBinding
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.main.ErrorCode
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleActivity : BaseActivity<ActivityAddShceduleBinding, AddScheduleViewModel>(), AddNavigator {
    override val TAG: String
        get() = AddScheduleActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_add_shcedule
    }

    override fun getViewModel(dataSource: DataSource): AddScheduleViewModel {
        intent.getSerializableExtra("SELECT_DAY")?.let {
            return AddScheduleViewModel(dataSource, this, it as DateTime)
        }
        intent.getParcelableExtra<Schedule>("SELECT_ITEM")?.let {
            return AddScheduleViewModel(dataSource, this, it)
        }
        return AddScheduleViewModel(dataSource, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_schedule, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.save_schedule -> viewModel.saveSchedule()
            else -> {
                Log.e(TAG, this.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewmodel = viewModel

        initStartEndContainer()

        initActionBar()

        initTimePicker()

        initCalendar()
    }

    private fun initStartEndContainer() {
        binding.llStartContainer.setOnClickListener {
            binding.tvStart.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorAddScheduleSelectDate))
            binding.tvEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlack))
            viewModel.OnStartDateClicked()
        }
        binding.llEndContainer.setOnClickListener {
            binding.tvStart.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlack))
            binding.tvEnd.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorAddScheduleSelectDate))
            viewModel.OnEndDateClicked()
        }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.toolbarAddSchedule)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initTimePicker() {
        val time = DateTime()
        if (Build.VERSION.SDK_INT >= 23) {
            binding.tpDate.hour = time.hourOfDay
            binding.tpDate.minute = time.minuteOfHour
        } else {
            binding.tpDate.currentHour = time.hourOfDay
            binding.tpDate.currentMinute = time.minuteOfHour
        }
        binding.tpDate.setOnTimeChangedListener { _, hourOfDay, minute ->
            viewModel.OnTimeChanged(hourOfDay, minute)
        }
    }

    private fun initCalendar() {
        intent.getSerializableExtra("SELECT_DAY")?.let {
            val date = it as DateTime
            binding.mcvSelectSchedule.currentDate = CalendarDay.from(date.year, date.monthOfYear, date.dayOfMonth)
            binding.mcvSelectSchedule.selectedDate = CalendarDay.from(date.year, date.monthOfYear, date.dayOfMonth)
        }
        intent.getParcelableExtra<Schedule>("SELECT_ITEM")?.let {
            val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA)
            val currentDate = DateTime(formatter.parse(it.startDate).time)
            binding.mcvSelectSchedule.currentDate =
                CalendarDay.from(currentDate.year, currentDate.monthOfYear, currentDate.dayOfMonth)
            binding.mcvSelectSchedule.selectedDate =
                CalendarDay.from(currentDate.year, currentDate.monthOfYear, currentDate.dayOfMonth)
        }
        binding.mcvSelectSchedule.apply {
            setOnMonthChangedListener { _, date ->
                binding.mcvSelectSchedule.selectedDate = date
                viewModel.OnDateChanged(date.year, date.month, date.day)
            }
            setOnDateChangedListener { _, date, _ ->
                viewModel.OnDateChanged(date.year, date.month, date.day)
            }
        }
    }

    override fun OnSaveFail(errorCode: ErrorCode) {
        Toast.makeText(this, errorCode.description, Toast.LENGTH_SHORT).show()
    }

    override fun OnSaveSuccess() {
        Toast.makeText(this, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }
}