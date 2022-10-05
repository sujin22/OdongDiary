package com.example.owndiary.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.owndiary.data.DiaryDataSource
import com.example.owndiary.model.Diary

class DiaryViewModel : ViewModel() {
    private var diaryList = mutableStateListOf<Diary>()

    init{
        diaryList.addAll(DiaryDataSource().loadDiaryList())
    }

    fun addDiary(diary: Diary){
        diaryList.add(diary)
    }

    fun removeDiary(diary: Diary){
        diaryList.remove(diary)
    }

    fun editDiary(index: Int, diary: Diary){
        Log.e("ImageCard_Edited", "$index 의 내용이 ${diaryList[index].title}에서")
        diaryList[index] = diary
        Log.e("ImageCard_Edited", "${diary.title}로 바뀌었습니다.")
    }

    fun getAllDiary(): List<Diary>{
        return diaryList;
    }
}