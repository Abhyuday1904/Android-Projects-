package com.example.foodwastagereductionapp


import FeedbackPage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.Room
import com.example.foodwastagereductionapp.data.AppDatabase

import com.example.foodwastagereductionapp.pages.ImageManagementPage
import com.example.foodwastagereductionapp.pages.LogOut
import com.example.foodwastagereductionapp.pages.MenuPage
import com.example.foodwastagereductionapp.pages.NavItem
import com.example.foodwastagereductionapp.pages.NavItemIcon
import com.example.foodwastagereductionapp.pages.TimingScreen


@Composable
fun HomePage(navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    var isExpanded by remember {mutableStateOf(false) } // Toggle state for expansion

    val NavItemList = listOf(
        NavItem("Home", NavItemIcon.VectorIcon(Icons.Default.Home)),
        NavItem("Timing", NavItemIcon.ResourceIcon(painterResource(id = R.drawable.timing_icon))),
        NavItem("Feedback", NavItemIcon.ResourceIcon(painterResource(id = R.drawable.feedback_icon_2))),
        NavItem("Log Out", NavItemIcon.VectorIcon(Icons.Default.ExitToApp)),
        NavItem("Gallery", NavItemIcon.VectorIcon(Icons.Default.Notifications))
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                tonalElevation = 8.dp,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                NavItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = (selectedIndex == index),
                        onClick = { selectedIndex = index },
                        icon = {
                            when (val icon = navItem.icon) {
                                is NavItemIcon.VectorIcon -> Icon(
                                    imageVector = icon.imageVector,
                                    contentDescription = navItem.label,
                                    modifier = Modifier.size(if (selectedIndex == index) 32.dp else 28.dp),
                                    tint = if (selectedIndex == index)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                is NavItemIcon.ResourceIcon -> Icon(
                                    painter = icon.painter,
                                    contentDescription = navItem.label,
                                    modifier = Modifier.size(if (selectedIndex == index) 32.dp else 28.dp),
                                    tint = if (selectedIndex == index)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        label = {
                            if (isExpanded) {
                                Text(
                                    text = navItem.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selectedIndex == index)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            authViewModel = authViewModel
        )
        ExpandCollapseButton(isExpanded, onToggle = { isExpanded = !isExpanded })
    }
}

@Composable
fun ExpandCollapseButton(isExpanded: Boolean, onToggle: () -> Unit) {
    Icon(
        imageVector = if (isExpanded) Icons.Default.Add else Icons.Default.Notifications,
        contentDescription = if (isExpanded) "Collapse" else "Expand",
        modifier = Modifier
            .padding(16.dp)
            .size(24.dp)
            .clickable { onToggle() },
        tint = MaterialTheme.colorScheme.primary
    )
}


@Composable
fun ContentScreen(modifier : Modifier = Modifier , selectedIndex : Int , authViewModel: AuthViewModel){
    val database = Room.databaseBuilder(
        LocalContext.current, // Use LocalContext to get the context
        AppDatabase::class.java,
        "app_database" // Database name
    ).build()
    val context = LocalContext.current
    when(selectedIndex){
        0 -> MenuPage()
        1 -> TimingScreen(context)
        2 -> FeedbackPage()
        3 -> LogOut(authViewModel = authViewModel)
        4 -> ImageManagementPage(context = context, database = database)

    }
}





