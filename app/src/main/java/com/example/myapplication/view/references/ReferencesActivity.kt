package com.example.myapplication.view.references

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityReferencesBinding
import com.example.myapplication.view.base.BaseActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_references.*
import java.util.*

class ReferencesActivity : BaseActivity<ActivityReferencesBinding, ReferenceListViewModel>() {

    override val TAG: String
        get() = ReferencesActivity::class.java.simpleName

    private val adapter: ReferenceListAdapter by lazy {
        ReferenceListAdapter(object : ReferenceItemClickListener {
            override fun itemOnClicked(path: String) {
                // TODO : FileDownload Worker를 이용해 실행
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        viewModel.init()

        initView()
        initListener()
    }

    override fun getLayoutID(): Int = R.layout.activity_references

    override fun getViewModel(dataSource: DataSource): ReferenceListViewModel = ReferenceListViewModel(dataSource)

    private fun initView() {

        rcv_references_list.apply {
            layoutManager = LinearLayoutManager(context,  RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = this@ReferencesActivity.adapter
        }
    }

    private fun initListener() {

        // toolbar back button listener
        toolbar_file_search.setNavigationOnClickListener { }

        //
        edit_file_search.setOnFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> fab_references_add.hide()
                else -> fab_references_add.show()
            }
        }

        edit_file_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterItem(edit_file_search.text.toString())
            }
        })


        fab_references_add.setOnClickListener {
            viewModel.addReference(Reference(
                "TEST1" + Random().nextInt().toString(),
                "TEST2" + Random().nextInt().toString()
            ))
        }
    }
}
