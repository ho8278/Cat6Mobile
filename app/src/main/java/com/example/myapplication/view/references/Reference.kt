/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import androidx.recyclerview.widget.DiffUtil
import java.util.*

data class Reference(val title: String, val path: String, val uploadDate: Date = Date(), val fileSize: Double = 0.0) {
    companion object {
        val DIFF_UTIL: DiffUtil.ItemCallback<Reference> = object : DiffUtil.ItemCallback<Reference>() {
            override fun areItemsTheSame(oldItem: Reference, newItem: Reference): Boolean =
                (oldItem == newItem) && (oldItem.path == newItem.path)

            override fun areContentsTheSame(oldItem: Reference, newItem: Reference): Boolean =
                (oldItem == newItem) && (oldItem.path == newItem.path)
        }
    }
}