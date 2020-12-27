package com.kkarabet.herpalooza.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kkarabet.herpalooza.R
import java.util.*

@Entity(tableName = "herps_table")
data class Herp(

    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "picture")
    var uuid:String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "type")
    var type: Int = 0,

    @ColumnInfo(name = "found")
    var found: Int = 0,

    @ColumnInfo(name = "species")
    var species: String = "",

    @ColumnInfo(name = "age")
    var age: String = "",

    @ColumnInfo(name = "sex")
    var sex: Int = 0,

    @ColumnInfo(name = "location_found")
    var location: String = "",

    @ColumnInfo(name = "place_found")
    var place: String = "",

    @ColumnInfo(name = "notes")
    var note: String = ""
)
{val defaultImage get() = R.drawable.gecko}


