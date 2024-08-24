package com.mohalim.edokan.ui.seller

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SellerAddProductActivity : AppCompatActivity() {
    val viewModel: SellerAddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SellerAddProductScreen(this, viewModel)
        }
    }
}

@Composable
fun SellerAddProductScreen(
    context: Context,
    viewModel: SellerAddProductViewModel,
) {
    val formState by viewModel.formState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = formState.productName,
            onValueChange = { viewModel.updateFormState{product->
                product.copy(productName = it)
            } },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = formState.productDescription,
            onValueChange = {
                viewModel.updateFormState {product->
                    product.copy(productDescription = it)
                } },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = formState.productPrice.toString(),
            onValueChange = {
                viewModel.updateFormState {product->
                    product.copy(productPrice = it.toDouble() ?: 0.0)
                } },
            label = { Text("Product Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = formState.productDiscount.toString(),
            onValueChange = { viewModel.updateFormState {product->
                product.copy(productDiscount = it.toDouble())
            } },
            label = { Text("Product Discount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = formState.productQuantity.toString(),
            onValueChange = { viewModel.updateFormState {product->
                product.copy(productQuantity = it.toDouble())
            } },
            label = { Text("Product Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        Button(
            onClick = {
                viewModel.addProduct()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Add Product")
        }

        formState.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colors.error)
        }

        if (formState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }


}




@Composable
fun Image(
    viewModel: SellerAddProductViewModel,
    onProductAdded: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val formState by viewModel.formState.collectAsState()
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val context = LocalContext.current
        val storageRef = FirebaseStorage.getInstance()

        // Image picker launcher
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            uri?.let {
                viewModel.uploadImageToFirebase(storageRef, it) { downloadUrl ->
                    viewModel.updateFormState { it.copy(productImage = downloadUrl) }
                }
            }
        }

        // Camera launcher
        val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            // Convert bitmap to Uri and upload to Firebase
            bitmap?.let {
                val uri = viewModel.saveBitmapToUri(context, it)
                imageUri = uri
                uri?.let {
                    viewModel.uploadImageToFirebase(storageRef, it) { downloadUrl ->
                        viewModel.updateFormState { it.copy(productImage = downloadUrl) }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Pick Image from Gallery")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { cameraLauncher.launch(null) }) {
                Text("Take a Picture")
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

            Button(
                onClick = {
                    viewModel.addProduct()
                    onProductAdded()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = formState.productName.isNotBlank() && formState.productPrice > 0f
            ) {
                Text("Add Product")
            }

            formState.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colors.error)
            }

            if (formState.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}