/*
 * Created by WonJongSeong on 2019-05-27
 */

package com.example.myapplication.view.references

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import io.reactivex.Single

class UploadTask(val context : Context, val workerParameters: WorkerParameters) : RxWorker(context, workerParameters) {
    override fun createWork(): Single<Result> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}