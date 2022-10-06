package com.example.owndiary

import android.annotation.SuppressLint
import android.database.CursorWindow
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
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.owndiary.ui.screen.DiaryViewModel
import com.example.owndiary.ui.screen.HomeScreen
import com.example.owndiary.ui.screen.SettingScreen
import com.example.owndiary.ui.screen.WriteDiaryScreen
import com.example.owndiary.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.lang.reflect.Field


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        lateinit var bitmapImage: Bitmap
        const val REVIEW_MIN_LENGTH = 10

        //갤러리 권한 요청
        const val REQ_GALLERY = 1

        //API 호출시 Paremeter key값
        const val PARAM_KEY_IMAGE = "image"
        const val PARAM_KEY_PRODUCT_ID = "product_id"
        const val PARAM_KEY_REVIEW = "review_content"
        const val PARAM_KEY_RATING = "rating"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setContent {
            OwnDiaryTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    val diaryViewModel: DiaryViewModel by viewModels()
                    bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.poster)
                    OwnDiaryApp(diaryViewModel)
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OwnDiaryApp(diaryViewModel: DiaryViewModel = viewModel()){
    val diaryList = diaryViewModel.diaryList
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
                    SettingScreen(modalBottomSheetState, coroutineScope)
                },
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxHeight(),
                    scaffoldState = scaffoldState,
                    content = { padding ->  // We need to pass scaffold's inner padding to content. That's why we use Box.
                        Box(modifier = Modifier.padding(padding)) {
                            HomeScreen(
                                diaryList.value,
                                coroutineScope, modalBottomSheetState, navController
                            )
                        }
                    }
                )
            }
        }
        composable("new_diary") {
            WriteDiaryScreen(
                onAddDiary = diaryViewModel::addDiary,
                isNew = true,
                navController = navController
            )
        }
        composable("detail_diary/{index}") {backStackEntry ->
            val indexStr = backStackEntry.arguments?.getString("index") ?:"-1"
            val index = indexStr.toInt();
            Log.e("ImageCard_Clicked", "Index is $index")

            WriteDiaryScreen(
                diary = diaryList.value[index],
                onRemoveDiary = diaryViewModel::removeDiary,
                onEditDiary = diaryViewModel::updateDiary,
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

private fun setImageBitmap(uri: Uri) {
//    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
//        ImageDecoder.decodeBitmap(
//            ImageDecoder.createSource(contentResolver, uri)
//        )
//    }else{
//        MediaStore.Images.Media.getBitmap(contentResolver, uri)
//    }
}
