package com.example.owndiary.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owndiary.model.Diary
import com.example.owndiary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DiaryRepository
    ): ViewModel() {
    private val _diaryList = MutableStateFlow<List<Diary>>(emptyList())
    val diaryList = _diaryList.asStateFlow()

    init{
        viewModelScope.launch(Dispatchers.IO){
            repository.getAllDiary().distinctUntilChanged()
                .collect{ listOfDiary ->
                    if(listOfDiary.isNullOrEmpty()){
                        Log.d("Empty", ": Empty list")
                    }else{
                        _diaryList.value = listOfDiary
                    }
                }
        }
    }

    fun removeAllDiary() = viewModelScope.launch {
        repository.deleteAllDiary()
    }
}