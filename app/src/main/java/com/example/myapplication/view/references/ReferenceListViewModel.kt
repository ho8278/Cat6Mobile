/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

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
            setViewReferences(originList)
            loadItemCountObservable.set(it.data.size.toString() + "개 파일")
        })

    fun uploadReferences(filePathList: MutableList<String>) : OneTimeWorkRequest? {
        // TODO : Worker 부르기

        if (filePathList.size == 0) return null

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

        return request
    }

    fun downloadReference(fileName: String) {

        val inputData: Data.Builder = Data.Builder()
        inputData.putString("fileName", fileName)

        val request: OneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadTask::class.java)
            .setInputData(inputData.build())
            .addTag("DOWNLOAD")
            .build()

        WorkManager.getInstance().enqueue(request)
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

    private fun setViewReferences(reference: List<Reference>) {
        referenceList.clear()
        referenceList.addAll(reference)
        loadItemCountObservable.set(reference.size.toString() + "개 파일")
    }

    private fun setReferences(reference: List<Reference>) {
        originList.clear()
        originList.addAll(reference)
    }

    fun filterItem(str: String) {
        if (::preSearchDisposable.isInitialized && !preSearchDisposable.isDisposed)
            preSearchDisposable.dispose()

        if (str.isNotEmpty()) {
            preSearchDisposable = Observable.fromIterable(originList)
                .filter { t -> t.title.contains(str) }
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setViewReferences(it)
                }, {
                    it.stackTrace[0]
                })
        } else {
            setViewReferences(originList)
        }
    }
}