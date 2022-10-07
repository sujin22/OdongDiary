package com.example.owndiary.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBar(
    pointColor: Color,
    title: String,
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState) {
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
                    tint = pointColor,
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
            text = title,
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
                tint = pointColor,
            )
        }
    }
}
