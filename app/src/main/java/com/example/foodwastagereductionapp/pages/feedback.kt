package com.example.foodwastagereductionapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FeedbackScreen(){
    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = " Feedback page will be shown in this screen")
    }
}