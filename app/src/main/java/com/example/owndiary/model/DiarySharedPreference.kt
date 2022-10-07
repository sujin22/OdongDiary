package com.example.owndiary.model

import android.content.Context
import android.content.SharedPreferences
import com.example.owndiary.R
import com.example.owndiary.ui.theme.Blue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DiarySharedPreference(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    /* GET */
    fun getThemeColor(): PaletteItem {
        val jsonThemeColor: String? = sharedPref.getString("themeColor", "")
        val gson = Gson()
        val themeColor: PaletteItem = if (!jsonThemeColor.isNullOrEmpty()) {
            gson.fromJson(jsonThemeColor, object : TypeToken<PaletteItem>() {}.type)
        } else Blue
        return themeColor
    }

    fun getDiaryName(): String {
        return sharedPref.getString("diaryName", "Own Diary")!!
    }

    /* SET
     * commit()은 동기, apply()는 비동기이므로 apply()가 더 빠른 속도로 처리가 가능하다.
     */
    fun setThemeColor(value: PaletteItem) {
        val gson = Gson()
        val json = gson.toJson(value)
        val editor = sharedPref.edit()
        editor.putString("themeColor", json)
        editor.apply()
    }

    fun setDiaryName(value: String) {
        sharedPref.edit().putString("diaryName", value).apply()
    }
}