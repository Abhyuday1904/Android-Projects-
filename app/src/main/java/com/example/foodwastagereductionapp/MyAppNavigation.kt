package com.example.foodwastagereductionapp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun MyAppNavigation(modifier: Modifier  = Modifier , authViewModel: AuthViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login", builder = {
        composable(route = "login"){
            LoginScreen(navController , authViewModel)
        }
        composable(route = "signIn"){
            SignIn_Screen(navController , authViewModel)
        }
        composable(route = "home"){
           HomePage(navController , authViewModel)
        }

    })
}