package com.example.owndiary.ui.screen

import androidx.activity.ComponentActivity
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
    val themeColor: PaletteItem = repository.getThemeColor()
    val diaryName: String = repository.getDiaryName()
    val diaryList = repository.getAllDiary()
}