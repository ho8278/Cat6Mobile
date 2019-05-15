package com.example.myapplication.view.addvote

import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityAddVoteBinding
import com.example.myapplication.view.base.BaseActivity

class AddVoteActivity:BaseActivity<ActivityAddVoteBinding,AddVoteViewModel>(){
    override val TAG: String
        get() = AddVoteActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_add_vote
    }

    override fun getViewModel(dataSource: DataSource): AddVoteViewModel {
        return AddVoteViewModel(dataSource)
    }
}