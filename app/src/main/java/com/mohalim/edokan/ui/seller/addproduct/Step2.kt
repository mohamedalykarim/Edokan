package com.mohalim.edokan.ui.seller.addproduct

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mohalim.edokan.R
import com.mohalim.edokan.ui.seller.SellerAddProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Composable
fun Step2(
    context: Context,
    viewModel: SellerAddProductViewModel,
    startForProfileImageResult: ActivityResultLauncher<Intent>,
) {
    val productName by viewModel.productName.collectAsState()
    val productDescription by viewModel.productDescription.collectAsState()
    var productPrice by remember {mutableStateOf("")}
    var productQuantity by remember {mutableStateOf("")}

    var isErrorProductName by rememberSaveable { mutableStateOf(false) }
    var isErrorProductDescription by rememberSaveable { mutableStateOf(false) }
    var isErrorProductPrice by rememberSaveable { mutableStateOf(false) }


    runBlocking(Dispatchers.IO) {
        if (viewModel.productPrice.value > 0.0){
            productPrice = viewModel.productPrice.value.toString()
        }

        if (viewModel.productQuantity.value > 0.0) {
            productQuantity = viewModel.productQuantity.value.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(parseColor("#f5f5f5"))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Fill the basic data for the product:",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Column {
                    OutlinedTextField(
                        value = productName,
                        placeholder = {
                            Text("Product Name", fontSize = 12.sp)
                        },
                        maxLines = 1,
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            errorContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = Color(parseColor("#CE0F1E")),
                            unfocusedLabelColor = Color(parseColor("#CE0F1E")),
                            errorLabelColor = Color(parseColor("#CE0F1E"))
                        ),
                        modifier = Modifier.shadow(elevation = 1.dp, shape = RoundedCornerShape(32.dp)),
                        shape = RoundedCornerShape(32.dp),
                        onValueChange = {
                            viewModel.setProductName(it)
                            if (!it.isNullOrEmpty()){
                                isErrorProductName = false
                            }
                        })

                    if (isErrorProductName) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 1.dp),
                            text = "You must enter a product name",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Column {
                    OutlinedTextField(
                        value = productDescription,
                        placeholder = {
                            Text("Product Description", fontSize = 12.sp)
                        },
                        maxLines = 1,
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            errorContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = Color(parseColor("#CE0F1E")),
                            unfocusedLabelColor = Color(parseColor("#CE0F1E")),
                            errorLabelColor = Color(parseColor("#CE0F1E"))
                        ),
                        modifier = Modifier.shadow(elevation = 1.dp, shape = RoundedCornerShape(32.dp)),
                        shape = RoundedCornerShape(32.dp),
                        onValueChange = {
                            viewModel.setProductDescription(it)
                            if (!it.isNullOrEmpty()){
                                isErrorProductDescription = false
                            }
                        })

                    if (isErrorProductDescription) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 1.dp),
                            text = "You must enter a product description",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Column {
                    OutlinedTextField(
                        value = productPrice,
                        placeholder = {
                            Text("Product Price", fontSize = 12.sp)
                        },
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            errorContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = Color(parseColor("#CE0F1E")),
                            unfocusedLabelColor = Color(parseColor("#CE0F1E")),
                            errorLabelColor = Color(parseColor("#CE0F1E"))
                        ),
                        modifier = Modifier.shadow(elevation = 1.dp, shape = RoundedCornerShape(32.dp)),
                        shape = RoundedCornerShape(32.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        maxLines = 1,
                        onValueChange = {
                            if (!it.equals("")){
                                viewModel.setProductPrice(it.toDouble())
                            }
                            productPrice = it

                            if (!it.isNullOrEmpty()){
                                isErrorProductPrice = false
                            }
                        })
                    if (isErrorProductPrice) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 1.dp),
                            text = "Product price must be greater than 0",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                OutlinedTextField(
                    value = productQuantity,
                    placeholder = {
                        Text("Product Quantity", fontSize = 12.sp)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        errorContainerColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedLabelColor = Color(parseColor("#CE0F1E")),
                        unfocusedLabelColor = Color(parseColor("#CE0F1E")),
                        errorLabelColor = Color(parseColor("#CE0F1E"))
                    ),
                    modifier = Modifier.shadow(elevation = 1.dp, shape = RoundedCornerShape(32.dp)),
                    shape = RoundedCornerShape(32.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    maxLines = 1,
                    onValueChange = {
                        if (!it.equals("")){
                            viewModel.setProductQuantity(it.toDouble())
                        }
                        productQuantity = it
                    })

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    border= BorderStroke(1.dp, Color(parseColor("#f9f9f9"))),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        if(productName.isNullOrEmpty()){
                            isErrorProductName = true
                        }
                        if(productDescription.isNullOrEmpty()){
                            isErrorProductDescription = true
                        }

                        if(productPrice.isNullOrEmpty()){
                            isErrorProductPrice = true
                        }else if (productPrice.toDouble() == 0.0){
                            isErrorProductPrice = true
                        }

                        if (!isErrorProductName && !isErrorProductDescription && !isErrorProductPrice){
                            viewModel.setCurrentStep(3)
                        }
                    }, colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(parseColor("#f6192a"),
                        ))
                ) {
                    Row {
                        Text("Next Step", modifier = Modifier.padding(top = 3.dp), fontSize = 12.sp, color = Color(parseColor("#f9f9f9")))
                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            Icons.Default.ChevronRight,
                            modifier = Modifier.size(30.dp),
                            tint = Color(parseColor("#f9f9f9")),
                            contentDescription = "Gallery"
                        )

                    }

                }
            }
        }
    }
}