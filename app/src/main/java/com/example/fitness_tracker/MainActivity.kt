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
                        tvStats.text = "No runs yet to display"
                    }
                    // last month
                    3 -> {
                        tvStats.text = "No runs yet to display"
                    }
                    // last year
                    4 -> {
                        tvStats.text = "No runs yet to display"
                    }
                    // all time
                    5 -> {
                        tvStats.text = "No runs yet to display"
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
            val client_id = "47634"
            val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", client_id)
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

    fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

        return calendar.time
    }
}
