package com.example.myapplication.view.mypage

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityMypageBinding
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.login.LoginActivity

class MyPageActivity:BaseActivity<ActivityMypageBinding,MyPageViewModel>(){
    override val TAG: String
        get() = MyPageActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_mypage
    }

    override fun getViewModel(dataSource: DataSource): MyPageViewModel {
        return MyPageViewModel(dataSource)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.logout->{
                val dialog = AlertDialog.Builder(this)
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("확인"){dialog, _->
                        viewModel.logout()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        dialog.dismiss()
                        finish()
                    }
                    .setNegativeButton("취소"){dialog,_->
                        dialog.cancel()
                    }.create()
                dialog.show()
            }
            android.R.id.home->{
                finish()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout,menu)
        return true
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