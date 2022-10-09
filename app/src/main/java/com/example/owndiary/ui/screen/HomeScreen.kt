package com.example.owndiary.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import com.example.owndiary.model.Diary
import com.example.owndiary.ui.components.ImageCard
import com.example.owndiary.ui.components.TopBar
import kotlinx.coroutines.launch
import java.time.LocalDateTime

enum class Sort{
    DESCENDING, ASCENDING, FAVORITES
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var sortState by remember { mutableStateOf(Sort.DESCENDING) }

    val diaryList = viewModel.diaryList.collectAsState(initial = emptyList())
        .value
        .filter{
            if(sortState == Sort.FAVORITES)
                it.isFavorite
            else
                true
        }
        .sortedWith(
            Comparator<Diary>{ d1, d2 ->
                if(sortState == Sort.DESCENDING){
                    d2.date.compareTo(d1.date)
                }else{
                    d1.date.compareTo(d2.date)
                }
            }
        )

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
                                backgroundColor = viewModel.themeColor.heavy,
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
                                .background(color = viewModel.themeColor.light)
                        ) {
                            TopBar(viewModel.themeColor.heavy, viewModel.diaryName,
                                coroutineScope, modalBottomSheetState,
                                onTapAscendingSort = {
                                    sortState = Sort.ASCENDING
                                },
                                onTapDescendingSort = {
                                    sortState = Sort.DESCENDING
                                },
                                onTapFavoritesSort = {
                                    sortState = Sort.FAVORITES
                                }
                            )
                            LazyVerticalGrid(
                                modifier = Modifier.fillMaxSize(),
                                columns = GridCells.Fixed(2),
                            ) {
                                itemsIndexed(diaryList) { index, item ->
                                    ImageCard(
                                        backgroundColor = viewModel.themeColor.middle,
                                        diary = item,
                                        onDiaryClicked = {
                                            Log.e("ImageCard_Clicked", "${item.id} Clicked")
                                            navController.navigate("detail_diary/${item.id}")
                                        },
                                        onTabFavorite = { isFavorite ->
                                            item.isFavorite = isFavorite
                                            viewModel.onTapFavorite(item)
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
