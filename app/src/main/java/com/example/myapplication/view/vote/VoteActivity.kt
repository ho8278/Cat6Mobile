package com.example.myapplication.view.vote

import android.content.Intent
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityVoteBinding
import com.example.myapplication.view.addvote.AddVoteActivity
import com.example.myapplication.view.base.BaseActivity

class VoteActivity:BaseActivity<ActivityVoteBinding,VoteViewModel>(){
    override val TAG: String
        get() = VoteActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_vote
    }

    override fun getViewModel(dataSource: DataSource): VoteViewModel {
        return VoteViewModel(dataSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView(){
        setSupportActionBar(binding.toolbarVote)

        supportActionBar?.title="채팅방 투표"

        binding.fabAddVote.setOnClickListener {
            val intent = Intent(this,AddVoteActivity::class.java)
            startActivity(intent)
        }

        //TODO:RecyclerView 어댑터, LayoutManager 등록
    }
}