package com.robolancers.lancerscoutkotlin.room.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity

@Entity(tableName = "teams")
data class Team(var teamNumber: Int?) : Parcelable{
    var id: Int = 0

    constructor(parcel: Parcel) : this(parcel.readValue(Int::class.java.classLoader) as? Int) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(teamNumber)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Team> {
        override fun createFromParcel(parcel: Parcel): Team {
            return Team(parcel)
        }

        override fun newArray(size: Int): Array<Team?> {
            return arrayOfNulls(size)
        }
    }
}