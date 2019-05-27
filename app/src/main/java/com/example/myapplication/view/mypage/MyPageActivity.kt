package com.example.myapplication.view.mypage

import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityMypageBinding
import com.example.myapplication.view.base.BaseActivity

class MyPageActivity:BaseActivity<ActivityMypageBinding,MyPageViewModel>(){
    override val TAG: String
        get() = MyPageActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_mypage
    }

    override fun getViewModel(dataSource: DataSource): MyPageViewModel {
        return MyPageViewModel(dataSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.viewmodel = viewModel
        viewModel.init()
    }
}