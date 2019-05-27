package com.example.myapplication.view.addteam

import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityAddTeamBinding
import com.example.myapplication.view.base.BaseActivity

class AddTeamActivity: BaseActivity<ActivityAddTeamBinding,AddTeamViewModel>(){
    override val TAG: String
        get() = AddTeamActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_add_team
    }

    override fun getViewModel(dataSource: DataSource): AddTeamViewModel {
        return AddTeamViewModel(dataSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.tvCancel.setOnClickListener {
            finish()
        }
        binding.tvCreateTeam.setOnClickListener {
            viewModel.createTeam(binding.tietTeamName.text.toString())
            finish()
        }
    }
}