package com.example.owndiary

import android.graphics.Paint.Align
import android.os.Bundle
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.owndiary.ui.theme.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            val modalBottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden
            )
            val scaffoldState = rememberScaffoldState()//scaffold state
            val coroutineScope = rememberCoroutineScope()

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home",
            ) {
                composable("home") {
                    ModalBottomSheetLayout(
                        modifier = Modifier.fillMaxHeight(),
                        sheetState = modalBottomSheetState,
                        sheetContent = {
                            SettingContent(modalBottomSheetState, coroutineScope)
                        },
                        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    ) {
                        Scaffold(
                            modifier = Modifier.fillMaxHeight(),
                            scaffoldState = scaffoldState,
                            content = { padding ->  // We need to pass scaffold's inner padding to content. That's why we use Box.
                                Box(modifier = Modifier.padding(padding)) {
                                    HomeScreen(coroutineScope, modalBottomSheetState, navController)
                                }
                            }
                        )
                    }
                }
                composable("new_diary") {
                    NewDiaryScreen(navController)
                }
            }

            BackHandler(enabled = modalBottomSheetState.isVisible) {
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    navController: NavController
) {
    var diaryList = rememberSaveable {
        mutableListOf(
            DiaryItem(R.drawable.poster, "2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!", false),
            DiaryItem(R.drawable.poster, "2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!", false),
            DiaryItem(R.drawable.poster, "2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!", false),
            DiaryItem(R.drawable.poster, "2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!", false),
            DiaryItem(R.drawable.poster, "2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!", false),
            DiaryItem(R.drawable.poster, "2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!", false),
            DiaryItem(R.drawable.poster, "2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!", false),
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = Blue.heavy,
                onClick = {
                    navController.navigate("new_diary")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "new",
                    tint = Color.Black
                )

            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Blue.light)
        ) {
            TopBar(coroutineScope, modalBottomSheetState)
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
            ) {
                itemsIndexed(
                    diaryList
                ) { index, item ->
                    ImageCard(
                        content = item.text,
                        favorite = item.isFavorite
                    )
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBar(coroutineScope: CoroutineScope, modalBottomSheetState: ModalBottomSheetState) {
    var isDropDownMenuExpended by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            IconButton(onClick = { isDropDownMenuExpended = true }) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "menu",
                    tint = Color.Black,
                )
            }
            DropdownMenu(
                modifier = Modifier.wrapContentSize(),
                expanded = isDropDownMenuExpended,
                onDismissRequest = {
                    isDropDownMenuExpended = false
                }) {
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text("날짜 오름차순")
                }
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text("날짜 내림차순")
                }
                DropdownMenuItem(onClick = { /*TODO*/ }) {
                    Text("Favorites")
                }
            }
        }
        Text(
            text = "Own Diary",
            fontSize = 25.sp,
        )
        IconButton(onClick = {
            coroutineScope.launch {
                modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
            }
        }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "setting",
                tint = Color.Black,
            )
        }
    }
}

@Composable
fun ImageCard(
    content: String,
    favorite: Boolean,
) {
    var isFavorite by rememberSaveable {
        mutableStateOf(favorite)
    }
    val onTabFavorite: (Boolean) -> Unit = { favorite ->
        isFavorite = favorite
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(10.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        backgroundColor = Blue.middle
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
                Image(
                    painterResource(id = R.drawable.poster),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                )
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
            Text(
                modifier = Modifier
                    .padding(0.dp, 5.dp),
                text = "2022.09.23",
                fontSize = 12.sp,
            )
            Text(
                text = content,
                fontSize = 12.sp,
                maxLines = 2,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingContent(modalBottomSheetState: ModalBottomSheetState, coroutineScope: CoroutineScope) {
    val (text, setValue) = remember {
        mutableStateOf("")
    }
    Surface(
        modifier = Modifier
            .fillMaxHeight(0.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blue.middle),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(10.dp))

            //확인 버튼
            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.End)
                    .padding(end = 30.dp),
                onClick = {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Blue.heavy),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "적용",
                )
            }
            Spacer(Modifier.height(10.dp))
            //Diary Name
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Blue.light
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Diary Name")
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BasicTextField(
                            value = text,
                            onValueChange = setValue,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            //Color Picker
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Blue.light
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Color")
                    Spacer(Modifier.height(10.dp))
                    PaletteCard()
                    Spacer(Modifier.height(8.dp))
                }
            }
            Spacer(Modifier.height(10.dp))

            //일기장 초기화 버튼
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    /*TODO: 일기장 초기화 클릭 시 동작*/
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Blue.light)
            ) {
                Text(
                    text = "일기장 초기화",
                    color = Color.Red,
                )
            }
        }
    }


}

@Composable
fun PaletteCard() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(
            listOf(
                Red, Pink, Yellow, Blue, Green, Purple, Navy
            )
        ) { index, item ->
            Button(
                modifier = Modifier
                    .size(40.dp, 40.dp)
                    .padding(5.dp, 5.dp),
                onClick = {
                    /*TODO: color 클릭 시 동작*/
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = item.heavy),
                shape = RoundedCornerShape(8.dp),
            ) {
            }
        }
    }
}

//Preview 없앨 때 defaultParameter 지우기
@Preview
@Composable
fun NewDiaryScreen(navController: NavController = rememberNavController()) {
    var isEditState by remember { mutableStateOf(true) }
    var isImageLoaded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .background(Blue.light)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                }
                )
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
                        isEditState = false
                        focusManager.clearFocus()
                        /*TODO: 내용 저장하기*/
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
        Text(
            text = "2022.10.02",
            color = Color.Black,
            fontSize = 17.sp,
//            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 10.dp),
        )

        //Title Area
        val (title, setTitle) = remember {
            mutableStateOf("")
        }
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

        //Context Area
        val (context, setContext) = remember {
            mutableStateOf("")
        }

        BasicTextField(
            value = context,
            onValueChange = setContext,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                if (context.isEmpty()) {
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
