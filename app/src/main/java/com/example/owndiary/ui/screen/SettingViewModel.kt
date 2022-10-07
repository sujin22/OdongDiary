package com.example.owndiary.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owndiary.model.PaletteItem
import com.example.owndiary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: DiaryRepository,
) : ViewModel(){
//    var settingThemeColor by mutableStateOf(repository.getThemeColor())
//    var settingDiaryName: String by mutableStateOf(repository.getDiaryName())
//
//    fun onRemoveAllDiary() = viewModelScope.launch {
//        repository.deleteAllDiary()
//    }
//    fun onClickPaletteItem(item: PaletteItem){
//        settingThemeColor = item
//    }
//    fun onClickApply(){
//        repository.setDiaryName(settingDiaryName)
//        repository.setThemeColor(settingThemeColor)
//    }
}