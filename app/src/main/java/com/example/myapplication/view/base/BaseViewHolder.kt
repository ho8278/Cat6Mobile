package com.example.myapplication.view.base

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource

abstract class BaseViewHolder<V : ViewDataBinding, M : BaseViewModel>(protected val binding: V, context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    protected var viewModel: M
    abstract fun getViewModel(dataManager: DataSource): M

    init {
        val dataManager: DataSource = DataManager.getInstance(context)
        viewModel = getViewModel(dataManager)
    }

}