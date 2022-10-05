package com.example.owndiary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.owndiary.ui.theme.*

@Composable
fun PaletteCard() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(
            listOf(
                Red, Pink, Yellow, Blue, Green, Purple, Navy
            )
        ) { index, item ->
            Button(
                modifier = Modifier
                    .size(40.dp, 40.dp)
                    .padding(5.dp, 5.dp),
                onClick = {
                    /*TODO: color 클릭 시 동작*/
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = item.heavy),
                shape = RoundedCornerShape(8.dp),
            ) {
            }
        }
    }
}
