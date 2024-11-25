package com.example.foodwastagereductionapp.pages


import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

import androidx.compose.material3.Text

import androidx.compose.runtime.remember

import com.example.foodwastagereductionapp.data.AppDatabase
import java.io.File
import java.io.FileOutputStream


import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodwastagereductionapp.data.ImagePost
import com.example.foodwastagereductionapp.data.NotificationHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
    return try {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
@Composable
fun ImageManagementPage(context: Context, database: AppDatabase) {
    var imagePosts by remember { mutableStateOf<List<ImagePost>>(emptyList()) }
    var showAddImageCard by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }


    val notificationHandler = NotificationHandler(context)


    // Load images from the database
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            imagePosts = database.imagePostDao().getAllImagePosts()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display existing image posts
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Image Posts",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(imagePosts) { post ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = post.description,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                bitmap = BitmapFactory.decodeFile(post.imagePath).asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Dimming backdrop when adding a new post
        if (showAddImageCard) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { showAddImageCard = false }
            )
        }

        // Add new post card
        if (showAddImageCard) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.9f),
                elevation = CardDefaults.cardElevation(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add New Post",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Button(
                        onClick = { launcher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Choose Image")
                    }

                    imageUri?.let {
                        Text(
                            text = "Image selected: ${it.lastPathSegment}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Enter Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )

                    Button(
                        onClick = {
                            imageUri?.let { uri ->
                                val imagePath = saveImageToInternalStorage(context, uri)
                                if (imagePath != null) {
                                    val imagePost = ImagePost(
                                        imagePath = imagePath,
                                        description = description,
                                        timestamp = System.currentTimeMillis()
                                    )


                                    CoroutineScope(Dispatchers.IO).launch {
                                        database.imagePostDao().insertImagePost(imagePost)
                                        withContext(Dispatchers.Main) {
                                            message = "Image saved!"
                                            imagePosts = database.imagePostDao().getAllImagePosts()

                                            val bitmap = BitmapFactory.decodeFile(imagePath)

                                            notificationHandler.showExpandedNotificationWithBigPicture(
                                                title = "Attention Students",
                                                description = description,
                                                bitmap = bitmap
                                            )
                                            imageUri = null
                                            description = ""
                                            showAddImageCard = false
                                        }
                                    }
                                } else {
                                    message = "Failed to save image!"
                                }
                            } ?: run {
                                message = "Please select an image."
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Save Image")
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { showAddImageCard = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 72.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Image")
        }
    }
}




