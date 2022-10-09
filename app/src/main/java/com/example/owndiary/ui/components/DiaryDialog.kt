package com.example.owndiary.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DiaryDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmText: String,
    dismissText: String,
    onClickConfirm:()->Unit,
    onClickDismiss:()->Unit={},
){
    AlertDialog(
        onDismissRequest = { },
        title={
            Text(title)
        },
        text = {
            Text(text)
        },
        confirmButton = {
            TextButton(onClickConfirm){
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClickDismiss){
                Text(dismissText)
            }
        }
    )
}