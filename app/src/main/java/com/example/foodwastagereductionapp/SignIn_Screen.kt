package com.example.foodwastagereductionapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodwastagereductionapp.ui.theme.FoodWastageReductionAppTheme



@Composable
fun SignIn_Screen(navController: NavController , authViewModel: AuthViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var ConfirmPassword by remember { mutableStateOf("") }
    var studentID by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value){
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context , (authState.value as AuthState.Error).message , Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Register", fontSize = 50.sp , fontStyle = FontStyle.Italic)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Create your account")
        Spacer(modifier = Modifier.height(40.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {email = it} ,
            label = { Text(text = "Enter your Email")},
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Info")}
            )
        OutlinedTextField(
            value = password,
            onValueChange = {password = it} ,
            label = { Text(text = "Enter your password")},
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Password Info")}
        )
        OutlinedTextField(
            value = ConfirmPassword,
            onValueChange = {ConfirmPassword = it} ,
            label = { Text(text = "Confirm your password")},
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Password Info")}
        )
        OutlinedTextField(
            value = studentID,
            onValueChange = {studentID = it} ,
            label = { Text(text = "Enter Student ID")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(imageVector = Icons.Default.Info, contentDescription = "Password Info")}
        )
        OutlinedTextField(
            value = number,
            onValueChange = {number = it} ,
            label = { Text(text = "Enter your contact number")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(imageVector = Icons.Default.Call, contentDescription = "Password Info")}
        )
        Spacer(modifier = Modifier.height(100.dp))

        Button(onClick = {
                         if(password == ConfirmPassword){
                             authViewModel.signUp(email, password, studentID, number , context)
                         }

            else{
                Toast.makeText(context , " Password mismatch !! " , Toast.LENGTH_SHORT).show()
            }

        }  , modifier = Modifier.width(300.dp)) {
            Text(text = "Sign In")
        }

        Row (modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center){
            Text(text = "     Already have an account ?" ,  modifier = Modifier.alignByBaseline() )
            TextButton(onClick = { navController.navigate("login") } ,  modifier = Modifier.alignByBaseline() ) {
                Text(text = "Login")
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun SignInPreview() {
//    FoodWastageReductionAppTheme {
//        SignIn_Screen(navController = rememberNavController() )
//    }
//}