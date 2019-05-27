package com.example.myapplication.view.references

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityReferencesBinding
import com.example.myapplication.util.FilePathProvider
import com.example.myapplication.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_references.*


class ReferencesActivity : BaseActivity<ActivityReferencesBinding, ReferenceListViewModel>() {

    override val TAG: String
        get() = ReferencesActivity::class.java.simpleName

    private val adapter: ReferenceListAdapter by lazy {
        ReferenceListAdapter(object : ReferenceItemClickListener {
            override fun itemOnClicked(path: String) {
                viewModel.downloadReference(path)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel
        viewModel.init()

        initView()
        initListener()

        viewModel.loadReferenceList()
    }

    override fun getLayoutID(): Int = R.layout.activity_references

    override fun getViewModel(dataSource: DataSource): ReferenceListViewModel = ReferenceListViewModel(dataSource)

    private fun initView() {


        rcv_references_list.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
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

            Intent().apply {
                type = "*/*"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    action = Intent.ACTION_OPEN_DOCUMENT
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                addCategory(Intent.CATEGORY_OPENABLE)
                startActivityForResult(this, 5050)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5050 && data != null && resultCode == Activity.RESULT_OK) {
            if (data.clipData != null) {
                val filePathList = mutableListOf<String>()
                for (count in IntRange(0, data.clipData.itemCount - 1)) {
                    val filePath = FilePathProvider.getPath(this, data.clipData.getItemAt(count).uri)
                    if (filePath != null)
                        filePathList.add(filePath)
                }
                // TODO : ViewModel 에게 FileUpload 요청하기
                viewModel.uploadReferences(filePathList)
            } else {
                data.extras
                val filePath = FilePathProvider.getPath(this, data.data)
                if (filePath != null)
                    viewModel.uploadReferences(mutableListOf(filePath))
            }
        }
    }
}
