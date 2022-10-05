package com.example.owndiary

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.owndiary.model.Diary
import com.example.owndiary.ui.screen.DiaryViewModel
import com.example.owndiary.ui.screen.HomeScreen
import com.example.owndiary.ui.screen.SettingScreen
import com.example.owndiary.ui.screen.WriteDiaryScreen
import com.example.owndiary.ui.theme.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
            OwnDiaryTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    val diaryViewModel: DiaryViewModel by viewModels()
                    OwnDiaryApp(diaryViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OwnDiaryApp(diaryViewModel: DiaryViewModel = viewModel()){
    val diaryList = diaryViewModel.getAllDiary()
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
                onAddDiary = diaryViewModel::addDiary,
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
                index = index,
                diary = if(index<diaryList.size) diaryList[index] else null,
                onRemoveDiary = diaryViewModel::removeDiary,
                onEditDiary = diaryViewModel::editDiary,
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
