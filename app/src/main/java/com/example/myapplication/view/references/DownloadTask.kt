/*
 * Created by WonJongSeong on 2019-05-27
 */

package com.example.myapplication.view.references

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.data.DataManager
import java.io.File

class DownloadTask(val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val dataManager: DataManager = DataManager.getInstance(context)

    override fun doWork(): Result {

        val fileName: String = inputData.getString("fileName")?:""

        if(fileName.isNotEmpty()) {
            val file : File = dataManager.downloadFile(fileName)
                .blockingGet()

            if(!file.exists())
                return Result.failure()
        }

        return Result.success()
    }
}