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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.asImageBitmap

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodwastagereductionapp.data.ImagePost

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
fun PostImagePageLocal(context: Context, database: AppDatabase) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Post Image Locally", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Choose Image")
        }

        imageUri?.let {
            Text("Image selected: ${it.lastPathSegment}")
        }

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Enter Description") },
            modifier = Modifier.fillMaxWidth()
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

                        // Save to Room Database
                        CoroutineScope(Dispatchers.IO).launch {
                            database.imagePostDao().insertImagePost(imagePost)
                            message = "Image saved locally!"
                        }
                    } else {
                        message = "Failed to save image!"
                    }
                } ?: run {
                    message = "Please select an image."
                }
            }
        ) {
            Text("Save Image Locally")
        }

        Text(message, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
    }
}


@Composable
fun DisplayImagesPage(database: AppDatabase) {
    var imagePosts by remember { mutableStateOf<List<ImagePost>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            imagePosts = database.imagePostDao().getAllImagePosts()
        }
    }

    LazyColumn {
        items(imagePosts) { post ->
            Text("Description: ${post.description}")
            Image(
                bitmap = BitmapFactory.decodeFile(post.imagePath).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        }
    }
}
