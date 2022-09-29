package com.example.owndiary

import android.graphics.Paint.Align
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val modalBottomSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden
            )
            val scaffoldState = rememberScaffoldState()//scaffold state
            val coroutineScope = rememberCoroutineScope()

            ModalBottomSheetLayout(
                modifier = Modifier.fillMaxHeight(),
                sheetState = modalBottomSheetState,
                sheetContent = {
                    SettingContent()
                },
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            ){
                Scaffold(
                    modifier = Modifier.fillMaxHeight(),
                    scaffoldState = scaffoldState,
                    content = { padding ->  // We need to pass scaffold's inner padding to content. That's why we use Box.
                        Box(modifier = Modifier.padding(padding)) {
                            HomeScreen(coroutineScope, modalBottomSheetState)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(coroutineScope: CoroutineScope, modalBottomSheetState: ModalBottomSheetState) {
    var diaryList = rememberSaveable {
        mutableListOf(
            DiaryItem(R.drawable.poster,"2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!",false),
            DiaryItem(R.drawable.poster,"2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!",false),
            DiaryItem(R.drawable.poster,"2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!",false),
            DiaryItem(R.drawable.poster,"2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!",false),
            DiaryItem(R.drawable.poster,"2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!",false),
            DiaryItem(R.drawable.poster,"2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!",false),
            DiaryItem(R.drawable.poster,"2022.09.23", "이건 내가 정말 좋아하는 영화인 어바웃타임의 포스터!!!",false),
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = Color(0xFF6799FF),
                onClick = {/*TODO*/ }
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
                .background(color = Color(0xFFEBF7FF))
        ) {
            TopBar(coroutineScope, modalBottomSheetState)
            LazyVerticalGrid(
                cells =GridCells.Fixed(2),
            ){
                itemsIndexed(
                    diaryList
                ){index, item ->
                    ImageCard(
                        content = item.text,
                        favorite = item.isFavorite
                    )
                }
            }

        }
    }

}

@Composable
fun SettingContent(){
    Surface(
        modifier = Modifier
            .fillMaxHeight(0.9f)
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB2CCFF)),
            horizontalAlignment = Alignment.CenterHorizontally,){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color(0xFFEBF7FF)
                
            ) {
                Column(modifier = Modifier.padding(20.dp)){
                    Text(
                        text = "Diary Name")
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            ),
                        value ="",
                        onValueChange = {},
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
        Column{
            IconButton(onClick = { isDropDownMenuExpended = true}) {
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
            coroutineScope.launch{
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
    content:String,
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
        backgroundColor = Color(0xFFB2CCFF)
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