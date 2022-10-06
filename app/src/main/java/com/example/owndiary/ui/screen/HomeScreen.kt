package com.example.owndiary.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.owndiary.ui.components.ImageCard
import com.example.owndiary.ui.components.TopBar
import com.example.owndiary.ui.theme.Blue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notes = viewModel.diaryList.collectAsState(
        initial = emptyList()
    ).value

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scaffoldState = rememberScaffoldState()//scaffold state
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        modifier = Modifier. fillMaxHeight(),
        sheetState = modalBottomSheetState,
        sheetContent = {
            SettingScreen(modalBottomSheetState, coroutineScope)
        },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxHeight(),
            scaffoldState = scaffoldState,
            content = { padding ->  // We need to pass scaffold's inner padding to content. That's why we use Box.
                Box(modifier = Modifier.padding(padding)) {
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
                                columns = GridCells.Fixed(2),
                            ) {
                                itemsIndexed(notes) { index, item ->
                                    ImageCard(
                                        diary = item,
                                        onDiaryClicked = {
                                            Log.e("ImageCard_Clicked", "${item.id} Clicked")
                                            navController.navigate("detail_diary/${item.id}")
                                        }
                                    )
                                }
                            }

                        }
                    }
                }
            }
        )
    }
    BackHandler(enabled = modalBottomSheetState.isVisible) {
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }
}
