package com.example.fitness_tracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_tracker.data.Run
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        var runs = mutableListOf<Run>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("shared_pref", Context.MODE_PRIVATE)

        val orig = sharedPref.getString("run_data", null)
        val type = object : TypeToken<MutableList<Run>>() {}.type
        val temp: MutableList<Run>? = Gson().fromJson(orig, type)

        if (temp != null) {
            runs = temp
        }

        spinnerTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val currDate = Calendar.getInstance().timeInMillis
                val weekMillis = 604800000L
                val monthMillis = 2419200000L
                val yearMillis = 31449600000L

                when (position) {
                    // select
                    0 -> {
                        tvStats.text = "No runs yet to display"
                    }
                    // most recent run
                    1 -> {
                        if (runs.size == 0) {
                            tvStats.text = "No runs yet to display"
                        } else {
                            val dist = "%.2f".format(runs[0].distance * 1.609f)
                            val timeH = "%.2f".format(runs[0].minutes / 60f)
                            tvStats.text = "Total distance = $dist km\nTotal time = $timeH hours"
                        }
                    }
                    // last week
                    2 -> {
                        var dist = 0f
                        var timeM = 0f

                        for (run in runs) {
                            val temp = Calendar.getInstance()
                            temp.set(run.year, run.month - 1, run.day)
                            val run_date = temp.timeInMillis
                            val diff = currDate - run_date

                            d("curr_date", Calendar.getInstance().time.toString())
                            d("run_date", temp.time.toString())
                            d("run_diff", diff.toString())

                            if (diff in 0..weekMillis) {
                                d("adding_run", "distance: ${run.distance}, time: ${run.minutes}")
                                dist += run.distance
                                timeM += run.minutes
                            } else {
                                break
                            }
                        }

                        val timeH = timeM / 60f
                        val timeHStr = "%.2f".format(timeH)
                        if (dist == 0f) {
                            tvStats.text = "No runs here to display"
                        } else {
                            val distStr = "%.2f".format(dist * 1.609f)
                            tvStats.text =
                                "Total distance = $distStr km\nTotal time = $timeHStr hours"
                        }
                    }
                    // last month
                    3 -> {
                        var dist = 0f
                        var timeM = 0f

                        for (run in runs) {
                            val temp = Calendar.getInstance()
                            temp.set(run.year, run.month - 1, run.day)
                            val run_date = temp.timeInMillis
                            val diff = currDate - run_date

                            if (diff in 0..monthMillis) {
                                d("adding_run", "distance: ${run.distance}, time: ${run.minutes}")
                                dist += run.distance
                                timeM += run.minutes
                            } else {
                                break
                            }
                        }

                        val timeH = timeM / 60f
                        val timeHStr = "%.2f".format(timeH)
                        if (dist == 0f) {
                            tvStats.text = "No runs here to display"
                        } else {
                            val distStr = "%.2f".format(dist * 1.609f)
                            tvStats.text =
                                "Total distance = $distStr km\nTotal time = $timeHStr hours"
                        }
                    }
                    // last year
                    4 -> {
                        var dist = 0f
                        var timeM = 0f

                        for (run in runs) {
                            val temp = Calendar.getInstance()
                            temp.set(run.year, run.month - 1, run.day)
                            val run_date = temp.timeInMillis
                            val diff = currDate - run_date

                            if (diff in 0..yearMillis) {
                                d("adding_run", "distance: ${run.distance}, time: ${run.minutes}")
                                dist += run.distance
                                timeM += run.minutes
                            } else {
                                break
                            }
                        }

                        val timeH = timeM / 60f
                        val timeHStr = "%.2f".format(timeH)
                        if (dist == 0f) {
                            tvStats.text = "No runs here to display"
                        } else {
                            val distStr = "%.2f".format(dist * 1.609f)
                            tvStats.text =
                                "Total distance = $distStr km\nTotal time = $timeHStr hours"
                        }
                    }
                    // all time
                    5 -> {
                        var dist = 0f
                        var timeM = 0f

                        for (run in runs) {
                            dist += run.distance
                            timeM += run.minutes
                        }

                        val timeH = timeM / 60f
                        val timeHStr = "%.2f".format(timeH)
                        if (dist == 0f) {
                            tvStats.text = "No runs here to display"
                        } else {
                            val distStr = "%.2f".format(dist * 1.609f)
                            tvStats.text =
                                "Total distance = $distStr km\nTotal time = $timeHStr hours"
                        }
                    }
                }
            }
        }

        //    { "token_type":"Bearer",
        //        "access_token":"c45f7f713c5122a772a9cd30933fa1086506f5b6",
        //        "expires_at":1589093322,
        //        "expires_in":21415,
        //        "refresh_token":"1c5eae10a247b2a4085858d40cd3b1cb45e583d0" }%

        btnOAuth.setOnClickListener {
            val clientId = "47634"
            val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", "https://fitness-tracker.com")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("approval_prompt", "auto")
                .appendQueryParameter("scope", "read,activity:read_all")
                .build()

            startActivity(Intent(Intent.ACTION_VIEW, intentUri))
            d("btnOAuth", "new intent started")
        }

        btnViewPlans.setOnClickListener {
            val intent = Intent(this@MainActivity, PlansActivity::class.java)
            startActivity(intent)
            finish()
            d("btnViewPlans", "new intent started")
        }
    }
}
