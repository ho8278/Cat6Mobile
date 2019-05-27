package com.example.myapplication.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Team
import com.example.myapplication.databinding.FragmentTeamlistBinding
import com.example.myapplication.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_teamlist.*
import java.util.*

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