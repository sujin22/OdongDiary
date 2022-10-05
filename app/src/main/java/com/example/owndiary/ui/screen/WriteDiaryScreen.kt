package com.example.owndiary.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owndiary.R
import com.example.owndiary.model.Diary
import com.example.owndiary.ui.theme.Blue
import com.example.owndiary.ui.theme.DarkGray
import com.example.owndiary.ui.theme.LightGray
import java.time.format.DateTimeFormatter

@Composable
fun WriteDiaryScreen(
    index: Int = -1,    //TODO: Room 적용 이후 삭제
    navController: NavController,
    isNew: Boolean,
    diary: Diary? = null,
    onAddDiary: (Diary) -> Unit = {},
    onRemoveDiary: (Diary) -> Unit = {},
    onEditDiary: (Int, Diary) -> Unit = {index, item ->},
) {
    var isEditState by remember { mutableStateOf(isNew) }
    var isImageLoaded by remember { mutableStateOf(!isNew) }
    val focusManager = LocalFocusManager.current

    val (title, setTitle) = remember {
        mutableStateOf(diary?.title?:"")
    }
    val (content, setContent) = remember {
        mutableStateOf(diary?.content?:"")
    }

    Column(
        modifier = Modifier
            .background(Blue.light)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },

        horizontalAlignment = Alignment.Start,
    ) {
        //bar area
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                modifier = Modifier
                    .wrapContentWidth(),
                onClick = {
                    navController.navigateUp()  //뒤로가기
                },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back",
                    tint = Blue.heavy,
                )
            }
            if (isEditState) {
                Button(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    onClick = {
                        //HomeScreen 리스트에 내용 저장하기
                        if(isNew){
                            //add
                            onAddDiary(
                                Diary(
                                    image = diary?.image,
                                    title = title,
                                    content = content,
                                )
                            )
                        }else{
                            //edit
                            if (diary != null) {
                                onEditDiary(
                                    //TODO: Room 적용 이후에는 id 통해 삭제해야 함
                                    index,
                                    Diary(
                                        image = diary?.image,
                                        title = title,
                                        content = content,
                                    )
                                )
                            }
                        }
                        navController.navigateUp()  //뒤로가기
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Blue.heavy)
                ) {
                    Text("완료")
                }

            } else {
                Row {
                    Button(
                        modifier = Modifier
                            .width(40.dp),
                        onClick = {
                            /*TODO: 이 일기 삭제하기*/
//                                  id로 삭제하는 쿼리로 수정
//                                  onRemoveDiary(diary.id)
                            if (diary != null) {
                                onRemoveDiary(diary)
                            }
                            navController.navigateUp()  //뒤로가기
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Blue.middle),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "back",
                            tint = Color.Black,
                        )
                    }
                    Button(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        onClick = {
                            isEditState = true
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Blue.middle)
                    ) {
                        Text("편집")
                    }
                }
            }
        }

        //Image area
        if (isImageLoaded) {
            Image(
                painterResource(id = R.drawable.poster),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(10.dp),
            )
        } else {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(10.dp),
                onClick = {
                    /*TODO: 갤러리에서 사진 불러오기*/
                    focusManager.clearFocus()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = LightGray)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "load",
                    tint = Blue.heavy,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        //Date Area
        diary?.date?.let {
            Text(
                text = it.format(DateTimeFormatter.ofPattern("yyyy.MM.dd EEE")),
                color = Color.Black,
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp),
            )
        }

        //Title Area
        val maxChar = 15

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            value = title,
            onValueChange = { newTitle ->
                if (!newTitle.contains("\n")) {
                    setTitle(newTitle.take(maxChar))
                }
                if (newTitle.length > maxChar) {
                    //maxChar보다 길어졌을 경우 아래로 포커싱 이동
                    //(maxChar 처리를 해주지 않으면, TextField가 empty하게 초기화되는 에러 발생함)
                    // -> predictive text 때문인 것으로 추정됨
                    focusManager.moveFocus(FocusDirection.Down)
                }
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            singleLine = true,
            maxLines = 1,
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = "제목을 입력하세요.",
                        color = DarkGray,
                        fontSize = 20.sp
                    )
                }
                innerTextField()
            },
            enabled = isEditState,
        )
        Text(
            text = "(${title.length}/$maxChar)",
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp, bottom = 10.dp),
            textAlign = TextAlign.End,
        )

        Divider(
            color = Blue.middle,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
        )

        //Content Area
        BasicTextField(
            value = content,
            onValueChange = setContent,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                if (content.isEmpty()) {
                    Text(
                        text = "내용을 입력하세요.",
                        color = DarkGray,
                        fontSize = 15.sp,
                    )
                }
                innerTextField()
            },
            enabled = isEditState,
        )
    }
}
