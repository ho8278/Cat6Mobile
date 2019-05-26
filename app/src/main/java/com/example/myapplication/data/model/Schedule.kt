package com.example.myapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity//(foreignKeys = arrayOf(ForeignKey(entity = Team::class, parentColumns = arrayOf("chatinfo_id"), childColumns = arrayOf("teamID"))))
data class Schedule(@PrimaryKey
                    @SerializedName("schedule_ID")
                    val id:String,
                    @SerializedName("schedule_start_date")
                    val startDate: String,
                    @SerializedName("schedule_end_date")
                    val endDate: String,
                    @SerializedName("schedule_contents")
                    val name:String,
                    @SerializedName("schedule_team_ID")
                    val teamID: String):Parcelable{
    constructor(source:Parcel):this(source.readString(),source.readString(),source.readString(),source.readString(),source.readString())

    companion object {
        @JvmField
        val CREATOR:Parcelable.Creator<Schedule> = object: Parcelable.Creator<Schedule>{
            override fun createFromParcel(source: Parcel): Schedule = Schedule(source)

            override fun newArray(size: Int): Array<Schedule?> = arrayOfNulls(size)
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.id)
        dest.writeString(this.startDate)
        dest.writeString(this.endDate)
        dest.writeString(this.name)
        dest.writeString(this.teamID)
    }

    override fun describeContents(): Int {
        return 0
    }
}