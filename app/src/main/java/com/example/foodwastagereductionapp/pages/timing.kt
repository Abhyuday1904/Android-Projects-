package com.example.foodwastagereductionapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TimingScreen(){
    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Mess timing will be shown in this screen")
    }
}