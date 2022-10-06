package com.example.owndiary.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owndiary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DiaryRepository
    ): ViewModel() {

    val diaryList = repository.getAllDiary()

//    private val _diaryList = MutableStateFlow<List<Diary>>(emptyList())
//    val diaryList = repository.getAllDiary()

//    init{
//        viewModelScope.launch(Dispatchers.IO){
//            repository.getAllDiary().distinctUntilChanged()
//                .collect{ listOfDiary ->
//                    if(listOfDiary.isNullOrEmpty()){
//                        Log.d("Empty", ": Empty list")
//                    }else{
//                        _diaryList.value = listOfDiary
//                    }
//                }
//        }
//    }

    fun removeAllDiary() = viewModelScope.launch {
        repository.deleteAllDiary()
    }
}