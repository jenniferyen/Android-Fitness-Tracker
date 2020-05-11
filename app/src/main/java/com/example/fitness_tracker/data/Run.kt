package com.example.fitness_tracker.data

import java.util.*

class Run(
    distance: Float,
    minutes: Float,
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
        this.distance = distance
        this.minutes = minutes
        this.month = month
        this.day = day
        this.year = year
        val pace = this.minutes / distance
        pace_mins = pace.toInt()
        pace_secs = ((pace - pace_mins) * 60).toInt()
        speed = 60f * distance / this.minutes
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
        return other.getCalDate().compareTo(this.getCalDate())
    }

    fun getCalDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month-1, day)
        return calendar.time
    }
}