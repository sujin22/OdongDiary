package com.example.owndiary.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owndiary.ui.components.PaletteCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingScreen(
    modalBottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight(0.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(viewModel.settingThemeColor.middle),
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
                    viewModel.onClickApply()
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.settingThemeColor.heavy),
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
                backgroundColor = viewModel.settingThemeColor.light
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
                            value = viewModel.settingDiaryName,
                            onValueChange = {
                                viewModel.settingDiaryName = it
                            },
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
                backgroundColor = viewModel.settingThemeColor.light
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Color")
                    Spacer(Modifier.height(10.dp))
                    PaletteCard(
                        onClickItem = { item ->
                            viewModel.onClickPaletteItem(item)
                        }
                    )
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
                    /*TODO: Dialog로 더블 체크*/
                    viewModel.onRemoveAllDiary()
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = viewModel.settingThemeColor.light)
            ) {
                Text(
                    text = "일기장 초기화",
                    color = Color.Red,
                )
            }
        }
    }


}
