package com.example.fitness_tracker

class AccessResponse (
    val token_type: String,
    val expires_at: String,
    val expires_in: String,
    val refresh_token: String,
    val access_token: String,
    val athlete: Athlete
)
