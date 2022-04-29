package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.session.MediaSession

class SunnyWeatherApplication:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        const val TOKEN = "yOjSU4y9C7xcUKiW\n"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}