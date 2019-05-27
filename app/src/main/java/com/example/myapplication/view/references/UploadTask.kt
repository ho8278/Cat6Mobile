/*
 * Created by WonJongSeong on 2019-05-27
 */

package com.example.myapplication.view.references

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.data.DataManager
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.CountDownLatch


class UploadTask(val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val dataManager: DataManager by lazy { DataManager.getInstance(context) }

/*
    override fun createWork(): Single<Result> {
        val filePathList = inputData.getStringArray("filePathList")

        if (filePathList?.size == 0) {
            return Single.just(Result.success())
        }

        val transformList: MutableList<MultipartBody.Part> = mutableListOf()

        filePathList!!.forEach {
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

        return Single.fromObservable<Result> {
            dataManager.uploadFile(transformList)
                .flatMapSingle {
                    Log.d("ZZZ")
                    when (it.responseCode) {
                        "200" -> Single.just(Result.success())
                        else -> Single.just(Result.failure())
                    }
                }
        }
    }*/

    override fun doWork(): Result {

        val filePathList = inputData.getStringArray("filePathList")

        if (filePathList?.size == 0) {
            return Result.success()
        }

        val countDownLatch = CountDownLatch(filePathList!!.size)

        if (filePathList?.size == 0) {
            return Result.success()
        }

        val transformList: MutableList<MultipartBody.Part> = mutableListOf()

        filePathList!!.forEach {
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

        dataManager.uploadFile(transformList)
            .doOnComplete { countDownLatch.countDown() }
            .doOnError { countDownLatch.countDown() }
            .flatMapSingle {
                when (it.responseCode) {
                    "200" -> Single.just(Result.success())
                    else -> Single.just(Result.failure())
                }
            }.toList().blockingGet()

        try {
            countDownLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return Result.success()
    }
}
