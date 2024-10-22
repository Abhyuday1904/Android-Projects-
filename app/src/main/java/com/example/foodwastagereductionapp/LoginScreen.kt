package com.example.foodwastagereductionapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodwastagereductionapp.ui.theme.FoodWastageReductionAppTheme

@Composable
fun LoginScreen(navController: NavController , authViewModel: AuthViewModel){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            val image = painterResource(id = R.drawable.geeta_residency)
            Image(painter = image , contentDescription = "Login page image " ,
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.CenterHorizontally) ,)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = email ,
                              leadingIcon = {
                                            Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
                              },
                            onValueChange = {email = it},
                            label = { Text(text = "Enter your Email ID")},
                            singleLine = true
            )
            OutlinedTextField(value = password ,
                onValueChange = {password = it},
                label = { Text(text = "Password")},
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.Right) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Forgot password              " )
                }
            }




                Spacer(modifier = Modifier.height(150.dp))

            Button(onClick = {
                             authViewModel.login(email , password)
            } , modifier = Modifier
                .height(40.dp)
                .width(300.dp)) {
                Text(text = "Login")
            }

            Row (modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center){
                    Text(text = "     Don't have an account ?" ,  modifier = Modifier.alignByBaseline() )
                    TextButton(onClick = { navController.navigate("signIn") } ,  modifier = Modifier.alignByBaseline() ) {
                        Text(text = "Sign Up")
                    }
            }
        }

}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    FoodWastageReductionAppTheme {
        LoginScreen(navController = rememberNavController() , authViewModel = AuthViewModel())
    }
}