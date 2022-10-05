package com.example.owndiary.screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.owndiary.MainActivity
import com.example.owndiary.components.ImageCard
import com.example.owndiary.components.TopBar
import com.example.owndiary.model.Diary
import com.example.owndiary.ui.theme.Blue
import kotlinx.coroutines.CoroutineScope

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
