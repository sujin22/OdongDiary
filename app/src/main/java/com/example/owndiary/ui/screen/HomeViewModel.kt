package com.example.owndiary.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owndiary.model.PaletteItem
import com.example.owndiary.repository.DiaryRepository
import com.example.owndiary.ui.theme.Blue
import com.example.owndiary.ui.theme.Green
import com.example.owndiary.ui.theme.Navy
import com.example.owndiary.ui.theme.Purple
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DiaryRepository
    ): ViewModel() {
    var themeColor by mutableStateOf(repository.getThemeColor())
    var diaryName: String by mutableStateOf(repository.getDiaryName())
    val diaryList = repository.getAllDiary()

    var settingThemeColor by mutableStateOf(repository.getThemeColor())
    var settingDiaryName: String by mutableStateOf(repository.getDiaryName())

    fun onRemoveAllDiary() = viewModelScope.launch {
        repository.deleteAllDiary()
    }
    fun onClickPaletteItem(item: PaletteItem){
        settingThemeColor = item
    }
    fun onClickApply(){
        repository.setDiaryName(settingDiaryName)
        repository.setThemeColor(settingThemeColor)
        diaryName = settingDiaryName
        themeColor = settingThemeColor
    }
}