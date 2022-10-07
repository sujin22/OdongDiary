package com.example.owndiary.ui.screen

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owndiary.model.Diary
import com.example.owndiary.model.cropCenterBitmap
import com.example.owndiary.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WriteDiaryViewModel @Inject constructor(
    private val repository: DiaryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    var diaryId: Int? = null

    var diary by mutableStateOf<Diary?>(null)

    var imageUri by mutableStateOf<Uri?>(null)

    var imageField by mutableStateOf<Bitmap?>(diary?.image)

    var titleField by mutableStateOf(diary?.title ?: "")

    var contentField by mutableStateOf(diary?.content ?: "")

    init{
        savedStateHandle.get<String>("index")?.let{ diaryId ->
            this.diaryId = diaryId.toInt()
            getDiaryById()
        }
    }

    fun getDiaryById(){
        viewModelScope.launch{
            diaryId?.let{
                diary = repository.getDiaryById(it)
                titleField = diary?.title ?: ""
                contentField = diary?.content ?: ""
                imageField = diary?.image
            }
        }
    }

    //새로운 다이어리 삽입 시 호출되는 메소드
    fun onAddDiary(){
        if(imageField==null || titleField.isBlank()){
            return
        }
        var newDiary = Diary(
            image = imageField?.let {
                //가운데 기준 400:300 비율로 최대한 크게 크롭하고, 1000*750 size로 줄인다.
                cropCenterBitmap(it, 400, 300)?.let { it1 ->
                    Bitmap.createScaledBitmap(it1, 1000, 750, true)
                }
            },
            title = titleField,
            content = contentField,
        )
        viewModelScope.launch{
            repository.addDiary(newDiary)
        }
    }

    //다이어리 수정 시 호출되는 메소드
    fun onEditDiary(){
        diary?.title = titleField
        diary?.content = contentField
        diary?.date = LocalDateTime.now()

        Log.d("UPDATE_DIARY", "id: ${diary?.id}")
        Log.d("UPDATE_DIARY", "title: ${diary?.title}")
        Log.d("UPDATE_DIARY", "content: ${diary?.content}")

        viewModelScope.launch{
            diary?.let { repository.updateDiary(it) }
        }
    }

    //다이어리 삭제 시 호출되는 메소드
    fun onRemoveDiary() = viewModelScope.launch {
        diary?.let {
            repository.deleteDiary(it)
        }
    }
}