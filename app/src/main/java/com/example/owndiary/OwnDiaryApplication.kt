package com.example.owndiary

import android.app.Application
import com.example.owndiary.model.DiarySharedPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OwnDiaryApplication : Application(){
    companion object {
        lateinit var sharedPreferences: DiarySharedPreference
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = DiarySharedPreference(applicationContext)
    }
}