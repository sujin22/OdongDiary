package com.example.owndiary.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owndiary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: DiaryRepository,
) : ViewModel(){
    fun onRemoveAllDiary() = viewModelScope.launch {
        repository.deleteAllDiary()
    }
}