package com.example.owndiary.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.owndiary.MainActivity
import com.example.owndiary.R
import com.example.owndiary.model.Diary
import com.example.owndiary.ui.theme.Blue
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageCard(
    diary: Diary,
    onDiaryClicked:() -> Unit,
) {
    var isFavorite by rememberSaveable {
        mutableStateOf(diary.isFavorite)
    }
    val onTabFavorite: (Boolean) -> Unit = { favorite ->
        isFavorite = favorite
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(10.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .clickable {},
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        backgroundColor = Blue.middle,
        onClick = {
            onDiaryClicked()
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                //Image area
                diary.image?.let {
                    Image(
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        bitmap = it.asImageBitmap(),
                    )
                }
                //Favorite area
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd,
                ) {
                    IconButton(onClick = {
                        onTabFavorite(!isFavorite)
                    }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription = "favorite",
                            tint = Color.White,
                        )
                    }
                }
            }
            //Date Area
            Text(
                modifier = Modifier
                    .padding(vertical = 5.dp),
                text = diary.date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd EEE")),
                fontSize = 12.sp,
            )
            //Title Area
            Text(
                modifier = Modifier
                    .padding(bottom = 5.dp),
                text = diary.title,
                fontSize = 12.sp,
            )

            //Content Area
            Text(
                text = diary.content+"\n".repeat(2),
                fontSize = 12.sp,
                maxLines = 2,
            )
        }
    }
}
