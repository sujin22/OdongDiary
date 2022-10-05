package com.example.owndiary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.owndiary.model.Diary
import com.example.owndiary.ui.theme.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    companion object {
        const val REVIEW_MIN_LENGTH = 10

        //갤러리 권한 요청
        const val REQ_GALLERY = 1

        //API 호출시 Paremeter key값
        const val PARAM_KEY_IMAGE = "image"
        const val PARAM_KEY_PRODUCT_ID = "product_id"
        const val PARAM_KEY_REVIEW = "review_content"
        const val PARAM_KEY_RATING = "rating"
    }
    /*
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            //이미지를 받으면 ImageView에 적용한다.
            val imageUri = result.data?.data
            imageUri?.let{
                //서버 업로드를 위해 파일 형태로 변환한다.
                imageFile = File(getRealPathFromURI(it))

                //이미지를 불러온다
                Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .apply(RequestOptions().override(500,500))
                    .into(binding.writeReviewLayout.addImageBtn)
            }
        }
    }
    */

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

            var diaryList = remember{
                mutableStateListOf<Diary>()
            }

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

                                    HomeScreen(
                                        diaryList,
                                        coroutineScope, modalBottomSheetState, navController
                                    )
                                }
                            }
                        )
                    }
                }
                composable("new_diary") {
                    WriteDiaryScreen(
                        onAddDiary = {
                            diaryList.add(it)
                        },
                        isNew = true,
                        navController = navController
                    )
                }
                composable("detail_diary/{index}") {backStackEntry ->
                    val indexStr = backStackEntry.arguments?.getString("index") ?:"-1"
                    val index = indexStr.toInt();
                    Log.e("ImageCard_Clicked", "Index is $index")

                    //remove했을 때 recomposition되어 title, content, date index 접근에서 outOfBounds 에러 발생
                    //-> 쿼리 적용 전, 임시 방편으로 예외 처리 해주었음
                    //TODO: 쿼리 적용 후, list 수정에 따른 리컴포지션으로 수정할 것
                    WriteDiaryScreen(
                        diary = if(index<diaryList.size) diaryList[index] else null,
                        onRemoveDiary = {
                            diaryList.removeAt(index)
                        },
                        onEditDiary = {diary ->
                            diaryList[index] =  diary
                        },
                        isNew = false,
                        navController = navController
                    )
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
    notes: List<Diary>,
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    navController: NavController
) {
    //For Gallery
    val context = LocalContext.current
    val activity = LocalContext.current as MainActivity

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {

        }
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
                itemsIndexed(notes) { index, item ->
                    ImageCard(
                        diary = item,
                        onDiaryClicked = {
                            Log.e("ImageCard_Clicked", "Yes It Click $index")
                            navController.navigate("detail_diary/$index")
                        }
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
                Image(
                    painterResource(id = R.drawable.poster),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                )
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
                text = diary.content,
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


@Composable
fun WriteDiaryScreen(
    navController: NavController,
    isNew: Boolean,
    diary: Diary? = null,
//    diaryImage: Bitmap? = null,
//    diaryTitle: String = "",
//    diaryContent: String = "",
//    diaryDate: LocalDateTime = LocalDateTime.now(),
    onAddDiary: (Diary) -> Unit = {},
    onRemoveDiary: () -> Unit = {},
    onEditDiary: (Diary) -> Unit = {},
) {
    var isEditState by remember { mutableStateOf(isNew)}
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
                            onEditDiary(
                                Diary(
                                    image = diary?.image,
                                    title = title,
                                    content = content,
                                )
                            )
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
                                  onRemoveDiary()
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
        Text(
            text = "2022.10.02",
            color = Color.Black,
            fontSize = 17.sp,
//            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 10.dp),
        )

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

private fun setImageBitmap(uri: Uri) {
//    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//        ImageDecoder.decodeBitmap(
//            ImageDecoder.createSource(contentResolver, uri)
//        )
//    }else{
//        MediaStore.Images.Media.getBitmap(contentResolver, uri)
//    }
}
