package com.example.myapplication.view.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.FragmentTeamlistBinding
import com.example.myapplication.view.base.BaseFragment

class TeamListFragment:BaseFragment<FragmentTeamlistBinding,TeamListViewModel>() {
    override val TAG: String
        get() = TeamListFragment::class.java.simpleName

    lateinit var listener:GroupChangeListener

    override fun getViewModel(dataManager: DataSource): TeamListViewModel {
        return TeamListViewModel(dataManager)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_teamlist
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewmodel=viewModel
        viewModel.init()
        listener=activity as GroupChangeListener
        binding.rvTeamlist.layoutManager=LinearLayoutManager(activity?.applicationContext, RecyclerView.VERTICAL,false)
        binding.rvTeamlist.adapter=TeamListAdapter(listener)

        binding.ivBackButton.setOnClickListener {
            (activity as MainViewRefresh).refresh()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.remove(this)
            transaction?.commit()
        }
    }
}