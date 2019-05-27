package com.example.myapplication.view.addusertoteam

import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.DialogAddUserToTeamBinding
import com.example.myapplication.view.addschedule.AddNavigator
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.main.ErrorCode

class AddUserToTeamActivity : BaseActivity<DialogAddUserToTeamBinding, AddUserToTeamViewModel>(),AddNavigator{
    override val TAG: String
        get() = AddUserToTeamActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.dialog_add_user_to_team
    }

    override fun getViewModel(dataSource: DataSource): AddUserToTeamViewModel {
        return AddUserToTeamViewModel(dataSource, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewmodel = viewModel

        binding.buttonSearch.setOnClickListener {
            viewModel.searchUser()
        }
        binding.tvCancel.setOnClickListener {
            finish()
        }
        binding.tvOk.setOnClickListener {
            viewModel.addUserToTeam()
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