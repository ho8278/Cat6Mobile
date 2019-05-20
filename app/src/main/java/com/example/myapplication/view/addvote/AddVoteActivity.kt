package com.example.myapplication.view.addvote

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityAddVoteBinding
import com.example.myapplication.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_add_vote.*

class AddVoteActivity : BaseActivity<ActivityAddVoteBinding, AddVoteViewModel>() {
    override val TAG: String
        get() = AddVoteActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_add_vote
    }

    override fun getViewModel(dataSource: DataSource): AddVoteViewModel {
        return AddVoteViewModel(dataSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewmodel = viewModel

        initView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home->finish()
            R.id.save_schedule->{
                val adapter = rv_vote.adapter as AddVoteAdapter
                viewModel.saveVote(adapter.getList())
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_schedule,menu)
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
            val adapter = AddVoteAdapter()
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

    }
}