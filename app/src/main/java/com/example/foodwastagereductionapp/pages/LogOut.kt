package com.example.foodwastagereductionapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.foodwastagereductionapp.AuthViewModel

@Composable
fun LogOut(authViewModel: AuthViewModel){
    Column(modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.Center ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = { authViewModel.signOut() }) {
            Text(text = "Sign Out")
        }

    }
}