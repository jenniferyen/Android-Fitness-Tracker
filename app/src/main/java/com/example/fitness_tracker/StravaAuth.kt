package com.example.fitness_tracker

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.File
import java.io.IOException

class StravaAuth : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.strava_auth)

        val data = intent.toUri(0)
        d("full_url", "url = $data")

        var i = 27
        while (!(data[i] == '=' && data.slice(i - 4 until i) == "code")) {
            i++
        }
        i++
        var j = i
        while (data[j] != '&') {
            j++
        }
        val code = data.slice(i until j)
        d("access_code", "code = $code")

        val client_id = "47634"
        val client_secret = "56efa9ec707ddbba56ef0f11cd024f9314e2813e"

        val http_client = OkHttpClient()
        val formBody: RequestBody = FormBody.Builder()
            .add("client_id", client_id)
            .add("client_secret", client_secret)
            .add("code", code)
            .add("grant_type", "authorization_code")
            .build()

        val request: Request = Request.Builder()
            .url("https://www.strava.com/oauth/token")
            .post(formBody)
            .build()

        http_client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                d("api_response", body)
                val gson = GsonBuilder().create()

                val output = gson.fromJson(body, AccessResponse::class.java)
                if (output == null) {
                    d("error", "output was null, returning home")
                }

                val internalStorage: File = getFilesDir()
                val stravaFile = File(internalStorage, "strava_auth.txt")
                stravaFile.createNewFile()
                stravaFile.writeText(output.access_token)
                d("access token", "is ${output.access_token}")

                runOnUiThread {
                    startActivity(Intent(applicationContext, StravaVerified::class.java))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                d("failure", "failed to get access token")
                runOnUiThread {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
            }

        })
    }
}