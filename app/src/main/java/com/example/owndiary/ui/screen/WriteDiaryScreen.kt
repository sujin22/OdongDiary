package com.example.owndiary.ui.screen

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.hilt.navigation.compose.hiltViewModel
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.owndiary.MainActivity
import com.example.owndiary.R
import com.example.owndiary.model.Diary
import com.example.owndiary.model.cropCenterBitmap
import com.example.owndiary.ui.components.DiaryDialog
import com.example.owndiary.ui.theme.DarkGray
import com.example.owndiary.ui.theme.LightGray
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WriteDiaryScreen(
    navController: NavController,
    isNew: Boolean,
    viewModel: WriteDiaryViewModel = hiltViewModel(),
) {
    var isEditState by remember { mutableStateOf(isNew) }
    var isImageLoaded by remember { mutableStateOf(!isNew) }
    var openDeleteDialog by remember{ mutableStateOf(false)}

    val focusManager = LocalFocusManager.current


    //For Gallery
    val context = LocalContext.current

    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.imageUri = uri
        }

    Column(
        Modifier
            .fillMaxSize()
            .background(viewModel.themeColor.light)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    )
    {
        //삭제 다이얼로그
        if(openDeleteDialog){
            DiaryDialog(
                onDismissRequest ={
                  openDeleteDialog = false
                },
                title = "삭제하시겠습니까?",
                text="삭제된 일기는 되돌릴 수 없습니다.",
                confirmText = "삭제",
                dismissText = "취소",
                onClickConfirm = {
                    openDeleteDialog = false
                    viewModel.onRemoveDiary()
                    navController.navigateUp()
                },
                onClickDismiss = {
                    openDeleteDialog = false
                }
            )
        }
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
                    tint = viewModel.themeColor.heavy,
                )
            }
            if (isEditState) {
                Button(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    onClick = {
                        //HomeScreen 리스트에 내용 저장하기
                        if (isNew) {
                            //add
                            viewModel.onAddDiary()
                        } else {
                            //edit
                            viewModel.onEditDiary()
                        }
                        navController.navigateUp()  //뒤로가기
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.themeColor.heavy)
                ) {
                    Text("완료")
                }

            } else {
                Row {
                    //삭제 버튼
                    Button(
                        modifier = Modifier
                            .width(40.dp),
                        onClick = {
                            openDeleteDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.themeColor.middle),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "back",
                            tint = Color.Black,
                        )
                    }
                    //편집 버튼
                    Button(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        onClick = {
                            isEditState = true
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.themeColor.middle)
                    ) {
                        Text("편집")
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                        .verticalScroll(rememberScrollState(), reverseScrolling = true)
                        .imePadding()
        ) {
            //Image area
            if (isImageLoaded) {
                viewModel.imageField?.let { bitmap ->
                    Image(
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        bitmap = bitmap.asImageBitmap(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(10.dp),
                    )
                }
            } else {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(10.dp),
                    onClick = {
                        takePhotoFromAlbumLauncher.launch("image/*")
                        focusManager.clearFocus()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "load",
                        tint = viewModel.themeColor.heavy,
                        modifier = Modifier.size(50.dp)
                    )
                }
                viewModel.imageUri?.let {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    viewModel.imageField = ImageDecoder.decodeBitmap(source)
                    isImageLoaded = true
                }
            }

            //Date Area
            viewModel.diary?.date?.let {
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
                value = viewModel.titleField,
                onValueChange = { newTitle ->
                    if (!newTitle.contains("\n")) {
                        viewModel.titleField = newTitle.take(maxChar)
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
                    if (viewModel.titleField.isEmpty()) {
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
            if (isEditState) {
                Text(
                    text = "(${viewModel.titleField.length}/$maxChar)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, bottom = 10.dp),
                    textAlign = TextAlign.End,
                )
            }

            Divider(
                color = viewModel.themeColor.middle,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            )

            //Content Area
            BasicTextField(
                value = viewModel.contentField,
                onValueChange = { viewModel.contentField = it },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                textStyle = TextStyle(fontSize = 15.sp),
                decorationBox = { innerTextField ->
                    if (viewModel.contentField.isEmpty()) {
                        Text(
                            text = "내용을 입력하세요",
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
}

