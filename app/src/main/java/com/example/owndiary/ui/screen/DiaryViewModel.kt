package com.example.owndiary.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.owndiary.data.DiaryDataSource
import com.example.owndiary.model.Diary
import com.example.owndiary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
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

    fun addDiary(diary: Diary) = viewModelScope.launch{
        repository.addDiary(diary)
    }
    fun updateDiary(diary: Diary) = viewModelScope.launch{
        Log.d("UPDATE_DIARY", "id: ${diary.id}")
        Log.d("UPDATE_DIARY", "title: ${diary.title}")
        Log.d("UPDATE_DIARY", "content: ${diary.content}")
        repository.updateDiary(diary)
    }
    fun removeDiary(diary: Diary) = viewModelScope.launch {
        repository.deleteDiary(diary)
    }

    fun removeAllDiary() = viewModelScope.launch {
        repository.deleteAllDiary()
    }

//    fun editDiary(index: Int, diary: Diary){
//        diaryList[index] = diary
//    }

//    fun getAllDiary(): List<Diary>{
//        return diaryList.value
//    }
}