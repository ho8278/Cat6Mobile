/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.view.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.ExecutionException


class ReferenceListViewModel(val dataSource: DataSource) : BaseViewModel(dataSource) {

    val referenceList: ObservableArrayList<Reference> = ObservableArrayList()
    val loadItemCountObservable: ObservableField<String> = ObservableField()
    private val originList = mutableListOf<Reference>()
    private lateinit var preSearchDisposable: Disposable

    fun init() {


    }

    fun loadReferenceList() = getCompositeDisposable().add(dataSource.loadReferences()
        .subscribe {
            setReferences(it.data)
            loadItemCountObservable.set(it.data.size.toString())
        })

    fun uploadReferences(filePathList: MutableList<String>) {
        // TODO : Worker 부르기

        if (filePathList.size == 0) return

        val transformList: MutableList<MultipartBody.Part> = mutableListOf()

        filePathList.forEach {
            val file = File(it)
            transformList.add(
                MultipartBody.Part.createFormData(
                    "file", file.name, RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                    )
                )
            )
        }

        val inputData: Data.Builder = Data.Builder()
        inputData.putStringArray("filePathList", filePathList.toTypedArray())

        val request: OneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadTask::class.java)
            .addTag("UPLOAD")
            .setInputData(inputData.build())
            .build()

        if (isSameWorkRunning("UPLOAD")) {
            WorkManager.getInstance().cancelAllWorkByTag("UPLOAD")
        }

        WorkManager.getInstance().enqueue(request)
    }

    fun downloadReference(fileName: String) {

        val inputData : Data.Builder = Data.Builder()
        inputData.putString("fileName", fileName)

        val request : OneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadTask::class.java)
            .setInputData(inputData.build())
            .addTag("DOWNLOAD")
            .build()

        WorkManager.getInstance().enqueue(request)
    }

    fun addReference(reference: Reference) {
        originList.add(reference)
        referenceList.clear()
        referenceList.addAll(originList)
    }

    private fun isSameWorkRunning(tag: String): Boolean {
        val workInfos = WorkManager.getInstance().getWorkInfosByTag(tag)

        try {
            val workList = workInfos.get()

            for (info in workList) {
                val state = info.state
                if ((state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)) {
                    return true
                }
            }
            return false
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return false
    }

    private fun setReferences(reference: List<Reference>) {
        originList.clear()
        originList.addAll(reference)
        referenceList.clear()
        referenceList.addAll(originList)
    }

    fun filterItem(str: String) {
        if (::preSearchDisposable.isInitialized && !preSearchDisposable.isDisposed)
            preSearchDisposable.dispose()

        preSearchDisposable = Observable.fromIterable(originList)
            .filter { t -> t.title.contains(str) }
            .toList()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("Test", it.size.toString())
                referenceList.clear()
                if (str.isEmpty()) {
                    referenceList.addAll(originList)
                } else {
                    referenceList.addAll(ArrayList(it))
                }
            }, {
                it.stackTrace[0]
            })
    }
}