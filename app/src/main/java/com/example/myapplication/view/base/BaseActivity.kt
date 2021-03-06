package com.example.myapplication.view.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.myapplication.data.DataSource
import com.example.myapplication.view.main.AppInitialize

abstract class BaseActivity<T : ViewDataBinding, M : BaseViewModel> :
    AppCompatActivity() {
    abstract val TAG: String;
    protected lateinit var binding: T
    protected lateinit var viewModel: M

    abstract fun getLayoutID(): Int

    abstract fun getViewModel(dataSource: DataSource): M

    fun init() {
        viewModel = getViewModel(AppInitialize.dataSource)
        binding = DataBindingUtil.setContentView(this, getLayoutID())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate")
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onResume() {
        Log.d(TAG, "OnResume")
        super.onResume()
    }

    override fun onStart() {
        Log.d(TAG, "OnStart")
        super.onStart()
    }

    override fun onPause() {
        Log.d(TAG, "OnPause")
        super.onPause()
    }

    override fun onRestart() {
        Log.d(TAG, "OnRestart")
        super.onRestart()
    }

    override fun onStop() {
        Log.d(TAG, "OnStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "OnDestroy")
        super.onDestroy()
        viewModel.onDestroy()
    }
}