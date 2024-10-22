package com.example.foodwastagereductionapp


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.foodwastagereductionapp.pages.FeedbackScreen
import com.example.foodwastagereductionapp.pages.LogOut
import com.example.foodwastagereductionapp.pages.MenuPage
import com.example.foodwastagereductionapp.pages.NavItem
import com.example.foodwastagereductionapp.pages.TimingScreen


@Composable
fun HomePage( navController: NavController , authViewModel: AuthViewModel){
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val NavItemList = listOf(
        NavItem("Home", Icons.Default.Home) ,
        NavItem("Timing", Icons.Default.Home) ,
        NavItem("Feedback", Icons.Default.Home) ,
        NavItem("Log Out" , Icons.Default.ExitToApp)
    )
    Scaffold(
        modifier = Modifier.fillMaxSize() ,
        bottomBar = {
            NavigationBar {
                NavItemList.forEachIndexed { index, navItem ->

                    NavigationBarItem(selected = (selectedIndex == index) ,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        } ,
                        label = {
                            Text(text = navItem.label)
                        }
                        )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding) , selectedIndex , authViewModel)
    }
}

@Composable
fun ContentScreen(modifier : Modifier = Modifier , selectedIndex : Int , authViewModel: AuthViewModel){
    when(selectedIndex){
        0 -> MenuPage()
        1 -> TimingScreen()
        2 -> FeedbackScreen()
        3 -> LogOut(authViewModel = authViewModel)
    }
}


