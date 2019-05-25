package com.example.myapplication.view.vote

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityVoteBinding
import com.example.myapplication.view.addvote.AddVoteActivity
import com.example.myapplication.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_vote.*

class VoteActivity:BaseActivity<ActivityVoteBinding,VoteViewModel>(){
    override val TAG: String
        get() = VoteActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_vote
    }
    companion object {
        val ADD_REQUEST_CODE = 20
    }

    override fun getViewModel(dataSource: DataSource): VoteViewModel {
        return VoteViewModel(dataSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = viewModel

        viewModel.init()

        initView()
    }

    private fun initView(){
        setSupportActionBar(binding.toolbarVote)

        supportActionBar?.title="채팅방 투표"

        binding.fabAddVote.setOnClickListener {
            val intent = Intent(this,AddVoteActivity::class.java)
            startActivityForResult(intent,ADD_REQUEST_CODE)
        }

        rv_votelist.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
            adapter = VoteListAdapter()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == ADD_REQUEST_CODE){
            viewModel.init()
        }
    }
}