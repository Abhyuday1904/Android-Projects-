import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodwastagereductionapp.R
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

// ViewModel to manage state
class FeedbackViewModel : ViewModel() {

    private val _mealOptIns = mutableStateMapOf(
        "Breakfast" to 0,
        "Lunch" to 0,
        "Snacks" to 0,
        "Dinner" to 0
    )

    val mealOptIns: Map<String, Int> get() = _mealOptIns

    private val maxOptIns = 10

    fun optIn(mealType: String) {
        _mealOptIns[mealType] = (_mealOptIns[mealType] ?: 0) + 1
    }

    fun getProgress(mealType: String): Float {
        return (_mealOptIns[mealType]?.toFloat() ?: 0f) / maxOptIns
    }
}

@Composable
fun FeedbackPage(viewModel: FeedbackViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Feedback Page",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        items(viewModel.mealOptIns.keys.toList()) { mealType ->
            FeedbackRow(mealType = mealType, onOptIn = { viewModel.optIn(mealType) })
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Meal Opt-Out Progress",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(viewModel.mealOptIns.keys.toList()) { mealType ->
            FeedbackProgressRow(
                mealType = mealType,
                progress = viewModel.getProgress(mealType)
            )
        }
    }
}

@Composable
fun FeedbackRow(mealType: String, onOptIn: () -> Unit) {
    var isOptedIn by remember { mutableStateOf(false) }

    // Lottie animation setup
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_1731951642084))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display meal type text
            Text(
                text = mealType,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Conditionally show "Opt In" button or Lottie animation
            if (!isOptedIn) {
                Button(
                    onClick = {
                        isOptedIn = true // Prevent multiple clicks
                        onOptIn() // Trigger parent logic
                    }
                ) {
                    Text("Opt Out")
                }
            } else {
                // Show Lottie animation after opting in
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier
                        .size(50.dp)
                        .offset(x = 10.dp),
                    iterations = 1 // Run the animation only once
                )
            }

        }
    }
}

@Composable
fun FeedbackProgressRow(mealType: String, progress: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mealType,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackPagePreview() {
    FeedbackPage()
}
