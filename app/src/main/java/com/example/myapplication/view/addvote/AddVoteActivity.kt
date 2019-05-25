package com.example.myapplication.view.addvote

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityAddVoteBinding
import com.example.myapplication.view.addschedule.AddScheduleNavigator
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.main.ErrorCode
import kotlinx.android.synthetic.main.activity_add_vote.*

class AddVoteActivity : BaseActivity<ActivityAddVoteBinding, AddVoteViewModel>(), AddScheduleNavigator {
    override val TAG: String
        get() = AddVoteActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_add_vote
    }

    override fun getViewModel(dataSource: DataSource): AddVoteViewModel {
        return AddVoteViewModel(dataSource, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewmodel = viewModel

        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.save_schedule -> {
                val adapter = rv_vote.adapter as AddVoteAdapter
                viewModel.saveVote(adapter.getList())
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_schedule, menu)
        return true
    }

    private fun initView() {
        setSupportActionBar(binding.toolbarAddVote)
        supportActionBar?.apply {
            title = "투표 생성"
            setDisplayHomeAsUpEnabled(true)
        }
        binding.rvVote.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.apply {
            val adapter = AddVoteAdapter(viewModel)
            rvVote.adapter = adapter
            llAddContainer.setOnClickListener {
                adapter.addList()
            }
            llContainer.setOnClickListener {
                if (cbPlural.isChecked)
                    cbPlural.isChecked = false
                else
                    cbPlural.isChecked = true
            }
        }

        tiet_startdate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s ?: ""
                val regex = Regex("(19|20)[0-9]{2}[.](0[1-9]|1[012])[.](0[1-9]|[12][0-9]|3[01])")
                if(!regex.matches(text)){
                    tiet_startdate.error = "YYYY.MM.DD 형식을 맞춰주세요"
                }
                else{
                    tiet_startdate.error = null
                }
            }
        })

        tiet_enddate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s ?: ""
                val regex = Regex("(19|20)[0-9]{2}[.](0[1-9]|1[012])[.](0[1-9]|[12][0-9]|3[01])")
                if(!regex.matches(text)){
                    tiet_enddate.error = "YYYY.MM.DD 형식을 맞춰주세요"
                }
                else{
                    tiet_enddate.error = null
                }
            }
        })

    }

    override fun OnSaveFail(errorCode: ErrorCode) {
        Toast.makeText(this, errorCode.description, Toast.LENGTH_SHORT).show()
    }

    override fun OnSaveSuccess() {
        Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
        finish()
    }
}