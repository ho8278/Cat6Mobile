/*
 * Created by WonJongSeong on 2019-05-27
 */

package com.example.myapplication.view.references

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("fileDownloadUri")
    val fileDownloadUrl: String,
    @SerializedName("fileType")
    val fileType: String,
    @SerializedName("size")
    val fileSize: Int
) {
}