package com.example.myapplication.view.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.view.main.AppInitialize

abstract class BaseFragment<V : ViewDataBinding, M : BaseViewModel> : Fragment() {
    abstract val TAG: String;
    protected lateinit var binding: V
    protected lateinit var viewModel: M

    abstract fun getViewModel(dataManager: DataSource): M
    abstract fun getLayoutId(): Int

    fun init(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        viewModel = getViewModel(AppInitialize.dataSource)
    }

    override fun onAttach(context: Context) {
        Log.d(TAG, "OnAttach")
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "OnCreateView")
        init(inflater, container)
        return binding.root
    }

    override fun onPause() {
        Log.d(TAG, "OnPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "OnPause")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "OnDestroy")
        viewModel.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG, "OnDetach")
        super.onDetach()
    }
}