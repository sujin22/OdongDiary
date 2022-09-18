package com.example.owndiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.owndiary.ui.theme.OwnDiaryTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val (text, setValue) = remember {
                mutableStateOf("")
            }

            val scaffoldState = rememberScaffoldState()//scaffold state
            val scope = rememberCoroutineScope() //coroutine state
            val keyboardController = LocalSoftwareKeyboardController.current

            Scaffold(
                scaffoldState = scaffoldState,
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //onValueChange = 값이 변했을떄 수행할 로직을 작성하는 함수
                    TextField(
                        value = text,
                        onValueChange = setValue,
                    )
                    Button(onClick = {
                        keyboardController?.hide()
                        //코루틴 실행g
                        scope.launch{
                            scaffoldState.snackbarHostState.showSnackbar("Hello $text")
                        }
                    }) {
                        Text("Click")
                    }
                }
            }
        }

    }
}

@Composable
fun ImageCard(
    modifier: Modifier = Modifier, //기본값 지정
    isFavorite: Boolean,
    onTabFavorite: (Boolean) -> Unit,//Callback 생성
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Box(
            modifier = Modifier.height(200.dp)
        ) {
            Image(
                painterResource(id = R.drawable.poster),
                contentDescription = "poster",
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd,
            ) {
                IconButton(onClick = {
                    //onTabFavorite.invoke(!isFavorite) //invoke는 생략 가능하다.
                    onTabFavorite(!isFavorite)
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite
                        else Icons.Default.FavoriteBorder,
                        contentDescription = "favorite",
                        tint = Color.White
                    )
                }
            }
        }
    }
}