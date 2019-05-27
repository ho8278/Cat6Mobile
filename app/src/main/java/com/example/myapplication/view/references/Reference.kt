/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class Reference(
    @SerializedName("file_name")
    val title: String,
    @SerializedName("file_team_ID")
    val path: String) {
    companion object {
        val DIFF_UTIL: DiffUtil.ItemCallback<Reference> = object : DiffUtil.ItemCallback<Reference>() {
            override fun areItemsTheSame(oldItem: Reference, newItem: Reference): Boolean =
                (oldItem == newItem) && (oldItem.path == newItem.path)

            override fun areContentsTheSame(oldItem: Reference, newItem: Reference): Boolean =
                (oldItem == newItem) && (oldItem.path == newItem.path)
        }
    }
}