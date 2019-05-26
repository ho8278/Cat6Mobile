package com.example.myapplication.view.detailvote

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityDetailVoteBinding
import com.example.myapplication.view.addvote.AddVoteViewModel
import com.example.myapplication.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_detail_vote.*

class DetailVote:BaseActivity<ActivityDetailVoteBinding, AddVoteViewModel>(){
    override val TAG: String
        get() = DetailVote::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_detail_vote
    }

    val VOTE_ID = "VOTE_ID"
    val VOTE_PLURAL = "VOTE_PLURAL"
    lateinit var voteID : String

    override fun getViewModel(dataSource: DataSource): AddVoteViewModel {
        return AddVoteViewModel(dataSource, voteID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voteID = intent.getStringExtra(VOTE_ID)
        val plural = intent.getBooleanExtra(VOTE_PLURAL,false)
        super.onCreate(savedInstanceState)
        binding.viewmodel = viewModel

        binding.rvVote.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        binding.rvVote.adapter = VoteItemListAdapter(plural)

        button_vote_accept.setOnClickListener {
            viewModel.acceptVote((binding.rvVote.adapter as VoteItemListAdapter).selectList)
        }

    }
}