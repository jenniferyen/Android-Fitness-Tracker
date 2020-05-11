package com.example.fitness_tracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: String,

    @ColumnInfo(name = "Mon") var Mon: String,
    @ColumnInfo(name = "Tues") var Tues: String,
    @ColumnInfo(name = "Wed") var Wed: String,
    @ColumnInfo(name = "Thurs") var Thurs: String,
    @ColumnInfo(name = "Fri") var Fri: String,
    @ColumnInfo(name = "Sat") var Sat: String,
    @ColumnInfo(name = "Sun") var Sun: String
) : Serializable