package com.example.myapplication.view.updateschedule

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
import com.example.myapplication.view.addschedule.AddNavigator
import com.example.myapplication.view.addschedule.AddScheduleViewModel
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.main.AppInitialize
import com.example.myapplication.view.main.ErrorCode
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.joda.time.format.DateTimeFormat

class UpdateScheduleActivity : BaseActivity<ActivityAddShceduleBinding, AddScheduleViewModel>(), AddNavigator {

    val UPDATE_SCHEDULE = "UPDATE_SCHEDULE"

    override val TAG: String
        get() = UpdateScheduleActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_add_shcedule
    }

    override fun getViewModel(dataSource: DataSource): AddScheduleViewModel {
        return AddScheduleViewModel(dataSource, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_update_schedule, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.update_schedule -> viewModel.updateSchedule()
            else -> {
                Log.e(TAG, this.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val schedule = intent.getParcelableExtra<Schedule>(UPDATE_SCHEDULE)
        viewModel = AddScheduleViewModel(AppInitialize.dataSource,this, schedule)
        binding.viewmodel = viewModel

        initStartEndContainer()
        initActionBar()
        initTimePicker(schedule)
        initCalendar(schedule)
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

    private fun initTimePicker(schedule: Schedule) {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")
        val date = formatter.parseDateTime(schedule.startDate)
        if (Build.VERSION.SDK_INT >= 23) {
            binding.tpDate.hour = date.hourOfDay
            binding.tpDate.minute = date.minuteOfHour
        } else {
            binding.tpDate.currentHour = date.hourOfDay
            binding.tpDate.currentMinute = date.minuteOfHour
        }
        binding.tpDate.setOnTimeChangedListener { _, hourOfDay, minute ->
            viewModel.OnTimeChanged(hourOfDay, minute)
        }
    }

    private fun initCalendar(schedule: Schedule) {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")
        val date = formatter.parseDateTime(schedule.startDate)
        binding.mcvSelectSchedule.selectedDate = CalendarDay.from(date.year, date.monthOfYear, date.dayOfMonth)
        binding.mcvSelectSchedule.setOnMonthChangedListener { _, date ->
            binding.mcvSelectSchedule.selectedDate = date
            viewModel.OnDateChanged(date.year, date.month, date.day)
        }
        binding.mcvSelectSchedule.setOnDateChangedListener { _, date, _ ->
            viewModel.OnDateChanged(date.year, date.month, date.day)
        }
    }

    override fun OnSaveFail(errorCode: ErrorCode) {
        Toast.makeText(this, errorCode.description, Toast.LENGTH_SHORT).show()
    }

    override fun OnSaveSuccess() {
        Toast.makeText(this, "성공적으로 저장 되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }
}