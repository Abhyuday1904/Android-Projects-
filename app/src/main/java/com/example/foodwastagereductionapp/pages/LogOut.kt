package com.example.foodwastagereductionapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

// Feedback entity for Room database
@Entity(tableName = "feedback")
data class Feedbackk(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    var upvotes: Int
)

// DAO interface for Room database
@Dao
interface FeedbackDao {
    @Query("SELECT * FROM feedback")
    fun getAllFeedback(): Flow<List<Feedbackk>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: Feedbackk)

    @Update
    suspend fun updateFeedback(feedback: Feedbackk)
}


// Review Page
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewPage(
    feedbackDao: FeedbackDao // DAO for managing feedback in Room database
) {
    var feedbackText by remember { mutableStateOf("") }
    var showEmoji by remember { mutableStateOf(false) }
    val feedbackList = feedbackDao.getAllFeedback().collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFF126EFF), Color(0xFFF5F5F5))))
            .padding(16.dp)
    ) {
        // Motivational Quote at the Top
        Text(
            text = "“Saving food starts with us.”",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // Feedback Form
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Share your feedback or suggestions about the mess food:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Text Input for Feedback
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    placeholder = { Text("Enter your feedback here...") },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF126EFF),
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                val coroutineScope = rememberCoroutineScope()

                // Submit Button
                Button(
                    onClick = {
                        if (feedbackText.isNotBlank()) {
                            coroutineScope.launch {
                                feedbackDao.insertFeedback(Feedbackk(text = feedbackText, upvotes = 0))
                                feedbackText = ""
                                showEmoji = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF126EFF)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Submit", color = Color.White)
                }
            }
        }

        // Emoji after Submission
        if (showEmoji) {
            Text(
                text = "Thank you for your feedback! 😊",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                color = Color(0xFF126EFF),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Display Feedback Suggestions
        Text(
            text = "Feedbacks:",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(feedbackList) { feedback ->
                FeedbackItemm(feedback, feedbackDao)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FeedbackItemm(feedback: Feedbackk, feedbackDao: FeedbackDao) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    feedback.upvotes++
                    feedbackDao.updateFeedback(feedback)
                }
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = feedback.text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Upvote Icon and Count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFE3F2FD), CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Upvote",
                    tint = Color(0xFF126EFF)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = feedback.upvotes.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF126EFF)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ReviewPagePreview() {
    // Mock implementation of FeedbackDao
    val mockFeedbackDao = object : FeedbackDao {
        override fun getAllFeedback() = flowOf(
            listOf(
                Feedbackk(id = 1, text = "Great food!", upvotes = 5),
                Feedbackk(id = 2, text = "Needs more variety.", upvotes = 2),
                Feedbackk(id = 3, text = "Loved the desserts!", upvotes = 8)
            )
        )

        override suspend fun insertFeedback(feedback: Feedbackk) {
            // Mock implementation
        }

        override suspend fun updateFeedback(feedback: Feedbackk) {
            // Mock implementation
        }
    }

    ReviewPage(feedbackDao = mockFeedbackDao)
}
