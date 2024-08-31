package com.mohalim.edokan.ui.seller.addproduct

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.storage.FirebaseStorage
import com.mohalim.edokan.core.utils.HandlerUtils

@Composable
fun Step1(
    context: Context,
    viewModel: ViewModel,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current
        val storageRef = FirebaseStorage.getInstance()

        // Image picker launcher
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            uri?.let {

            }
        }

        // Camera launcher
        val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            // Convert bitmap to Uri and upload to Firebase
            bitmap?.let {
                imageUri = HandlerUtils.saveBitmapToFile(it, context)
            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            androidx.compose.material.Button(onClick = { launcher.launch("image/*") }) {
                androidx.compose.material.Text("Pick Image from Gallery")
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material.Button(onClick = { cameraLauncher.launch(null) }) {
                androidx.compose.material.Text("Take a Picture")
            }

            Spacer(modifier = Modifier.height(8.dp))

            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it)
                            .size(Size.ORIGINAL)
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                )
            }

            // Other input fields...

            Spacer(modifier = Modifier.height(16.dp))

        }



    }
}