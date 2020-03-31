package com.robolancers.lancerscoutkotlin.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pit_templates")
data class PitTemplate(var name: String?, var data: String?) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(data)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PitTemplate> {
        override fun createFromParcel(parcel: Parcel): PitTemplate {
            return PitTemplate(parcel)
        }

        override fun newArray(size: Int): Array<PitTemplate?> {
            return arrayOfNulls(size)
        }
    }
}