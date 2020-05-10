package com.example.fitness_tracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

//    { "token_type":"Bearer",
//        "access_token":"c45f7f713c5122a772a9cd30933fa1086506f5b6",
//        "expires_at":1589093322,
//        "expires_in":21415,
//        "refresh_token":"1c5eae10a247b2a4085858d40cd3b1cb45e583d0" }%

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOAuth.setOnClickListener {
            d("btOAuth", "btnOAuth was clicked")
            val client_id = "47634"
            val intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                .buildUpon()
                .appendQueryParameter("client_id", client_id)
                .appendQueryParameter("redirect_uri", "https://fitness-tracker.com")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("approval_prompt", "auto")
                .appendQueryParameter("scope", "read,activity:read_all")
                .build()

            val intent = Intent(Intent.ACTION_VIEW, intentUri)
            startActivity(intent)
            d("btnOAuth", "new intent started")
        }
    }
}
