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
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.databinding.ActivityAddShceduleBinding
import com.example.myapplication.view.main.ErrorCode
import com.prolificinteractive.materialcalendarview.CalendarDay

class AddScheduleActivity : BaseActivity<ActivityAddShceduleBinding, AddScheduleViewModel>(), AddScheduleNavigator {
    override val TAG: String
        get() = AddScheduleActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_add_shcedule
    }

    override fun getViewModel(dataSource: DataSource): AddScheduleViewModel {
        return AddScheduleViewModel(dataSource)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_schedule, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.home -> finish()
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
        if (Build.VERSION.SDK_INT >= 23) {
            binding.tpDate.hour = 8
            binding.tpDate.minute = 0
        } else {
            binding.tpDate.currentHour = 8
            binding.tpDate.currentMinute = 0
        }
        binding.tpDate.setOnTimeChangedListener { _, hourOfDay, minute ->
            viewModel.OnTimeChanged(hourOfDay, minute)
        }
    }

    private fun initCalendar() {
        binding.mcvSelectSchedule.selectedDate = CalendarDay.today()
        binding.mcvSelectSchedule.setOnMonthChangedListener { _, date ->
            binding.mcvSelectSchedule.selectedDate = date
            viewModel.OnDateChanged(date.month, date.day)
        }
        binding.mcvSelectSchedule.setOnDateChangedListener { _, date, _ ->
            viewModel.OnDateChanged(date.month, date.day)
        }
    }

    override fun OnSaveFail(errorCode: ErrorCode) {
        Toast.makeText(this,errorCode.description,Toast.LENGTH_SHORT).show()
    }

    override fun OnSaveSuccess() {
        Toast.makeText(this,"성공적으로 저장되었습니다.",Toast.LENGTH_SHORT).show()
        finish()
    }
}