package com.example.fitness_tracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness_tracker.data.Activity
import com.example.fitness_tracker.data.Run
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*

class StravaVerified : AppCompatActivity() {

    val http_client = OkHttpClient()
    var runs = mutableListOf<Run>()
    var activityIds = sortedSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.strava_auth)

        val internalStorage: File = filesDir
        val access_file = File(internalStorage, "strava_auth.txt")
        var scanner = Scanner(access_file)
        if (!scanner.hasNext()) {
            d("error", "no access token found")
            startActivity(Intent(this, MainActivity::class.java))
        }
        val access_token = scanner.next()
        d("access_token", access_token)
        scanner.close()

        // Get run data
        val sharedPreferences = getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        val gson = Gson()

        val orig = sharedPreferences.getString("run_data", null)
        val type = object : TypeToken<MutableList<Run>>() {}.type
        val temp: MutableList<Run>? = gson.fromJson(orig, type)
        if (temp != null) {
            runs = temp
        }

        for (run in runs) {
            if (run.id.isNotEmpty()) {
                activityIds.add(run.id)
            }
        }

        getListOfRuns(1, access_token)
    }

    fun getListOfRuns(page: Int, access_token: String) {
        val url = "https://www.strava.com/api/v3/activities"
        d("url visit", "visiting url ${"$url?access_token=$access_token&per_page=50&page=$page"}")

        val request = Request.Builder()
            .url("$url?access_token=$access_token&per_page=50&page=$page")
            .build()

        http_client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                d("api_response", body)
                val gson = GsonBuilder().create()

                val activities = gson.fromJson(body, Array<Activity>::class.java).toList()

                for (activity in activities) {
                    if (activity.type == "Run" && !activityIds.contains(activity.id)) {
                        val distance = activity.distance / 1609.34f
                        val minutes = activity.moving_time / 60f
                        val date = activity.start_date
                        val year = date.slice(0 until 4).toInt()
                        val month = date.slice(5 until 7).toInt()
                        val day = date.slice(8 until 10).toInt()
                        val title = activity.name
                        val run = Run(distance, minutes, month, day, year, title)
                        d("run_data", run.toString())
                        run.id = activity.id
                        runs.add(run)
                    }
                }

                if (activities.isNotEmpty()) {
                    getListOfRuns(page + 1, access_token)
                } else {
                    runOnUiThread {
                        runs.sort()
                        val json = gson.toJson(runs)
                        val sharedPreferences =
                            getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("run_data", json)
                        editor.apply()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                d("failure", "failed to access Strava data, returning to home screen")

                runOnUiThread {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
            }
        })
    }
}
