package com.example.fitness_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.*

class StravaVerified : AppCompatActivity() {

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
        d("access token: ", "$access_token")
        scanner.close()


    }

}
