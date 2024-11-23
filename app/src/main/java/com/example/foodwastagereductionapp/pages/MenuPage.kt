package com.example.foodwastagereductionapp.pages

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodwastagereductionapp.data.Meal

import com.example.foodwastagereductionapp.ui.theme.fontFamily
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MessMenuScreen(meal: Meal) {

//    val colors = listOf(Color(0xFFF17A18), Color(0xFF0089BD) , Color(0xFFF17A18) )
//    val gradient = Brush.verticalGradient(colors)

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0.1f,
        pageCount = {4}
    )
    val mealTypes = listOf(
        "Breakfast" to meal.meals.breakfast,
        "Lunch" to meal.meals.lunch,
        "Snacks" to meal.meals.snacks,
        "Dinner" to meal.meals.dinner
    )

    var selectedDate by remember {
        mutableStateOf(meal.date)
    }
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF126EFF),
                    Color(0xFF126EFF).copy(0.9f),
                    Color(0xFF126EFF).copy(0.8f),
                    Color(0xFF126EFF).copy(0.6f),
                    Color(0xFF126EFF).copy(0.5f),
//                           Color.Transparent

                )
            )
        )){
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.1f)
            .height(70.dp) , horizontalArrangement = Arrangement.Center , verticalAlignment = Alignment.Top) {
            IconButton(onClick = {
                showDatePicker(context){date ->
                    selectedDate = date
                     }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Date Picker",
                    tint = Color.White
                )
            }
            //Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Date Picker")
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = meal.day,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.9f),
            pageSpacing = 16.dp
        ) {
                page ->
            val (label, content) = mealTypes[page]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = label,
                        fontFamily = fontFamily,
                        fontSize = 35.sp,
                        textAlign = TextAlign.Start ,
                        color = Color.White,
                        modifier = Modifier.padding(start = 10.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))


                Card(modifier = Modifier
                    .size(width = 300.dp, height = 300.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp),
                    elevation = CardDefaults.cardElevation(20.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                    Box(modifier = Modifier.fillMaxSize()){
                        MealItem(label = label, content = content)
                    }
                }
            }
        }
    }

   }


@Composable
fun MenuPage() {
    // Load JSON from assets
    val jsonString = loadJsonFromAsset(LocalContext.current, "mess_menu.json")

    // Deserialize JSON to a list of Meal objects
    val menuList: List<Meal> = Json.decodeFromString(jsonString)

    // Get current date in the required format
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Find today's meal based on the current date
    val todayMeal = menuList.find { it.date == currentDate }

    // Display today's menu
    todayMeal?.let {
        MessMenuScreen(todayMeal)
    } ?: Text("No menu available for today.")


}

fun loadJsonFromAsset(context: Context, filename: String): String {
    return context.assets.open(filename).bufferedReader().use { it.readText() }
}

@Composable
fun MealItem(label: String, content: String) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 1.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
     //   Text(text = "$label:", fontSize = 18.sp, modifier = Modifier.padding(bottom = 4.dp))

        // Split content by comma and display each item on a new line
        content.split(",").forEach { item ->
            Row {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "nil")
                Text(
                    text = item.trim(),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                )
            }

        }
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            onDateSelected(dateFormat.format(calendar.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MenuPage()
}

