package com.example.owndiary

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.owndiary.model.DiarySharedPreference
import dagger.hilt.android.HiltAndroidApp
import java.util.prefs.Preferences

@HiltAndroidApp
class OwnDiaryApplication : Application(){
    companion object {
        lateinit var sharedPreferences: DiarySharedPreference
//        val Context.dataStore by preferencesDataStore(name = "settingPref")
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = DiarySharedPreference(applicationContext)
    }
}