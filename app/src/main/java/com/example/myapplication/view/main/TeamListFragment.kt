package com.example.myapplication.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.FragmentTeamlistBinding
import com.example.myapplication.view.base.BaseFragment

class TeamListFragment:BaseFragment<FragmentTeamlistBinding,TeamListViewModel>() {
    override val TAG: String
        get() = TeamListFragment::class.java.simpleName

    val listener = activity as GroupChangeListener

    override fun getViewModel(dataManager: DataSource): TeamListViewModel {
        return TeamListViewModel(dataManager)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_teamlist
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.init()
    }
}