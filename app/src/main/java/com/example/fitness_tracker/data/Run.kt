package com.example.fitness_tracker.data

import java.util.*

class Run(
    dist: Float,
    total_mins: Float,
    month: Int,
    day: Int,
    year: Int,
    title: String
) : Comparable<Run> {
    var distance = 0f
    var minutes = 0f
    var month = 0
    var day = 0
    var year = 0
    var pace_mins = 0
    var pace_secs = 0
    var speed = 0f
    var title = ""
    var id = ""

    init {
        distance = dist
        minutes = total_mins
        this.month = month
        this.day = day
        this.year = year
        val pace = minutes / dist
        pace_mins = pace.toInt()
        pace_secs = ((pace - pace_mins) * 60).toInt()
        speed = 60f * dist / minutes
        this.title = title
    }

    override fun toString(): String {
        val hrs = (minutes / 60).toInt()
        val mins_rem = minutes - hrs * 60
        val mins = mins_rem.toInt()
        val secs = ((mins_rem - mins) * 60).toInt()
        return "Date - %d/%d/%d\nDistance - %.2f miles\nTotal Time - %02d:%02d:%02d\nPace - %d:%02d minutes/mile\nAverage Speed - %.2f mi/hr".format(
            month,
            day,
            year,
            distance,
            hrs,
            mins,
            secs,
            pace_mins,
            pace_secs,
            speed
        )
    }

    override fun compareTo(other: Run): Int {
        return other.getDate().compareTo(this.getDate())
    }

    fun getDate(): Date {
        return Date(year - 1900, month - 1, day)
    }
}