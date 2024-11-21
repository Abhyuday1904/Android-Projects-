package com.example.foodwastagereductionapp.pages

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodwastagereductionapp.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@Composable
fun TimingScreen(context: Context) {
    val mealTimingsFlow = MealTimingRepository.getMealTimings(context)
    val mealTimings by mealTimingsFlow.collectAsState(initial = mapOf())

    val showPasswordDialog = remember { mutableStateOf(false) }
    val selectedMeal = remember { mutableStateOf<String?>(null) }
    val showEditDialog = remember { mutableStateOf(false) }
    val enteredPassword = remember { mutableStateOf("") }

    // Coroutine scope for suspend function calls
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF2196F3), Color(0xFF21CBF3))))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Meal Timings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            mealTimings.forEach { (meal, timing) ->
                MealTimingCard(
                    mealName = meal,
                    timing = timing,
                    onEditClick = {
                        selectedMeal.value = meal
                        showPasswordDialog.value = true
                    }
                )
            }
        }

        // Password Dialog
        if (showPasswordDialog.value) {
            PasswordDialog(
                enteredPassword = enteredPassword.value,
                onPasswordChange = { enteredPassword.value = it },
                onConfirm = {
                    if (enteredPassword.value == "admin@geetaResidency") {
                        showPasswordDialog.value = false
                        enteredPassword.value = ""
                        showEditDialog.value = true // Show edit dialog on success
                    } else {
                        enteredPassword.value = ""
                        Toast.makeText(context , "Wrong Password entered" , Toast.LENGTH_SHORT).show()

                    }
                },
                onDismiss = {
                    showPasswordDialog.value = false
                    enteredPassword.value = ""
                }
            )
        }

        // Edit Timing Dialog
        if (showEditDialog.value) {
            selectedMeal.value?.let { meal ->
                EditTimingDialog(
                    mealName = meal,
                    currentTiming = mealTimings[meal] ?: "",
                    onConfirm = { newTiming ->
                        // Save timing in DataStore within a coroutine
                        coroutineScope.launch {
                            MealTimingRepository.saveMealTiming(context, meal, newTiming)
                        }
                        showEditDialog.value = false
                    },
                    onDismiss = { showEditDialog.value = false }
                )
            }
        }
    }
}

@Composable
fun MealTimingCard(mealName: String, timing: String, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = mealName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = timing,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Timing",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PasswordDialog(
    enteredPassword: String,
    onPasswordChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Admin Authentication") },
        text = {
            Column {
                Text("Enter admin password to edit meal timings.")
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.TextField(
                    value = enteredPassword,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditTimingDialog(
    mealName: String,
    currentTiming: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newTiming by remember { mutableStateOf(currentTiming) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Edit Timing: $mealName") },
        text = {
            Column {
                Text("Current Timing: $currentTiming")
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.TextField(
                    value = newTiming,
                    onValueChange = { newTiming = it },
                    label = { Text("New Timing") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(newTiming) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}




private val Context.dataStore by preferencesDataStore(name = "meal_timings")

object MealTimingRepository {
    private val MEAL_TIMING_KEYS = listOf(
        stringPreferencesKey("breakfast"),
        stringPreferencesKey("lunch"),
        stringPreferencesKey("snacks"),
        stringPreferencesKey("dinner")
    )

    fun getMealTimings(context: Context): Flow<Map<String, String>> {
        return context.dataStore.data.map { preferences ->
            MEAL_TIMING_KEYS.mapIndexed { index, key ->
                val meal = listOf("Breakfast", "Lunch", "Snacks", "Dinner")[index]
                meal to (preferences[key] ?: "Not Set")
            }.toMap()
        }
    }

    suspend fun saveMealTiming(context: Context, meal: String, timing: String) {
        val key = MEAL_TIMING_KEYS[listOf("Breakfast", "Lunch", "Snacks", "Dinner").indexOf(meal)]
        context.dataStore.edit { preferences ->
            preferences[key] = timing
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TimingPagePreview() {
    val context = LocalContext.current
    TimingScreen(context)
}
