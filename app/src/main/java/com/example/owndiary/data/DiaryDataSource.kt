package com.example.owndiary.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.example.owndiary.DiaryItem
import com.example.owndiary.R
import com.example.owndiary.model.Diary

class DiaryDataSource {
    fun loadDiaryList(): List<Diary>{
        return listOf(
            Diary(null, "0.나의 최애 영화!", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!룰루랄라 정말 재미있고 감동적인 영화고, 배울 점이 많은 영화에요. 삶에 대한 생각을 깊게 해볼 수 있는 기회가 되어주는 영화이지요", false),
            Diary(null, "1.나의 최애 영화!", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!룰루랄라 정말 재미있고 감동적인 영화고, 배울 점이 많은 영화에요. 삶에 대한 생각을 깊게 해볼 수 있는 기회가 되어주는 영화이지요", false),
            Diary(null, "2.나의 최애 영화!", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!룰루랄라 정말 재미있고 감동적인 영화고, 배울 점이 많은 영화에요. 삶에 대한 생각을 깊게 해볼 수 있는 기회가 되어주는 영화이지요", false),
            Diary(null, "3.나의 최애 영화!", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!룰루랄라 정말 재미있고 감동적인 영화고, 배울 점이 많은 영화에요. 삶에 대한 생각을 깊게 해볼 수 있는 기회가 되어주는 영화이지요", false),
            Diary(null, "4.나의 최애 영화!", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!룰루랄라 정말 재미있고 감동적인 영화고, 배울 점이 많은 영화에요. 삶에 대한 생각을 깊게 해볼 수 있는 기회가 되어주는 영화이지요", false),
        )
    }
}