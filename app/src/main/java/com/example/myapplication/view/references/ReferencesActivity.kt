package com.example.myapplication.view.references

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityReferencesBinding
import com.example.myapplication.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_references.*

class ReferencesActivity : BaseActivity<ActivityReferencesBinding, ReferenceListViewModel>() {

    override val TAG: String
        get() = ReferencesActivity::class.java.simpleName

    private val adapter: ReferenceListAdapter by lazy { ReferenceListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        initView()
        initListener()
    }

    override fun getLayoutID(): Int = R.layout.activity_references

    override fun getViewModel(dataSource: DataSource): ReferenceListViewModel = ReferenceListViewModel(dataSource)

    private fun initView() {
        rcv_references_list.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rcv_references_list.adapter = adapter
    }

    private fun initListener() {

        // toolbar back button listener
        toolbar_file_search.setNavigationOnClickListener {  }

        edit_file_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            // editText textChange listener for file search
            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}
