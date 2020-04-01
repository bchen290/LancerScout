package com.robolancers.lancerscoutkotlin.room.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "scout_data", foreignKeys = [ForeignKey(entity = Team::class,
                                                 parentColumns = ["teamNumber"],
                                                 childColumns = ["teamNumber"],
                                                 onDelete = ForeignKey.CASCADE)])
data class ScoutData(
    @ColumnInfo(name = "teamNumber") var teamNumber: Int?,
    @ColumnInfo(name = "data") var data: String?
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(teamNumber)
        parcel.writeString(data)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScoutData> {
        override fun createFromParcel(parcel: Parcel): ScoutData {
            return ScoutData(parcel)
        }

        override fun newArray(size: Int): Array<ScoutData?> {
            return arrayOfNulls(size)
        }
    }
}