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
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

@Composable
fun Step3(
    context: Context,
    viewModel: SellerAddProductViewModel,
    startForProfileImageResult: ActivityResultLauncher<Intent>,
) {
    var productWidth by remember {mutableStateOf("0")}
    var productHeight by remember {mutableStateOf("0")}
    var productWeight by remember {mutableStateOf("0")}
    var productLength by remember {mutableStateOf("0")}
    var productDiscount by remember {mutableStateOf("0")}

    val holderUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.resources.getResourcePackageName(R.drawable.image_placeholder))
        .appendPath(context.resources.getResourceTypeName(R.drawable.image_placeholder))
        .appendPath(context.resources.getResourceEntryName(R.drawable.image_placeholder))
        .build()

    val image1Uri by viewModel.image1Uri.collectAsState(holderUri)
    val image2Uri by viewModel.image2Uri.collectAsState(holderUri)
    val image3Uri by viewModel.image3Uri.collectAsState(holderUri)
    val image4Uri by viewModel.image4Uri.collectAsState(holderUri)


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
                    text = "Optional data you can fill:",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = productWidth,
                    label = {
                        Text("Product Width", fontSize = 12.sp)
                    },
                    onValueChange = {
                        if (!it.equals("")){
                            viewModel.setProductWidth(it.toDouble())
                        }
                        productWidth = it

                    })

                OutlinedTextField(
                    value = productHeight,
                    label = {
                        Text("Product Height", fontSize = 12.sp)
                    },
                    onValueChange = {
                        if (!it.equals("")) {
                            viewModel.setProductHeight(it.toDouble())
                        }
                        productHeight = it
                    })

                OutlinedTextField(
                    value = productWeight,
                    label = {
                        Text("Product Weight", fontSize = 12.sp)
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    maxLines = 1,
                    onValueChange = {
                        if (!it.equals("")){
                            viewModel.setProductWeight(it.toDouble())
                        }
                        productWeight = it
                    })

                OutlinedTextField(
                    value = productLength,
                    label = {
                        Text("Product Length", fontSize = 12.sp)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    maxLines = 1,
                    onValueChange = {
                        if (!it.equals("")){
                            viewModel.setProductLength(it.toDouble())
                        }
                        productLength = it
                    })

                OutlinedTextField(
                    value = productDiscount,
                    label = {
                        Text("Product Discount", fontSize = 12.sp)
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    maxLines = 1,
                    onValueChange = {
                        if (!it.equals("")){
                            viewModel.setProductDiscount(it.toDouble())
                        }
                        productDiscount = it
                    })

                Row {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image1Uri)
                            .size(Size.ORIGINAL)
                            .build(),
                        placeholder = painterResource(id = R.drawable.image_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(100.dp)
                            .padding(16.dp)
                            .clickable {
                                viewModel.setImageProcess("IMAGE_1")
                                ImagePicker
                                    .with(context as AppCompatActivity)
                                    .crop()
                                    .cropSquare()
                                    .compress(1024)
                                    .maxResultSize(1080,1080)
                                    .createIntent {
                                        startForProfileImageResult.launch(it)
                                    }
                            },
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image2Uri)
                            .size(Size.ORIGINAL)
                            .build(),
                        placeholder = painterResource(id = R.drawable.image_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(100.dp)
                            .padding(16.dp)
                            .clickable{
                                viewModel.setImageProcess("IMAGE_2")
                                ImagePicker
                                    .with(context as AppCompatActivity)
                                    .crop()
                                    .cropSquare()
                                    .compress(1024)
                                    .maxResultSize(1080,1080)
                                    .createIntent {
                                        startForProfileImageResult.launch(it)
                                    }
                            }
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image3Uri)
                            .size(Size.ORIGINAL)
                            .build(),
                        placeholder = painterResource(id = R.drawable.image_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(100.dp)
                            .padding(16.dp)
                            .clickable{
                                viewModel.setImageProcess("IMAGE_3")
                                ImagePicker
                                    .with(context as AppCompatActivity)
                                    .crop()
                                    .cropSquare()
                                    .compress(1024)
                                    .maxResultSize(1080,1080)
                                    .createIntent {
                                        startForProfileImageResult.launch(it)
                                    }
                            }
                    )
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(image4Uri)
                            .size(Size.ORIGINAL)
                            .build(),
                        placeholder = painterResource(id = R.drawable.image_placeholder),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .height(100.dp)
                            .padding(16.dp)
                            .clickable{
                                viewModel.setImageProcess("IMAGE_4")
                                ImagePicker
                                    .with(context as AppCompatActivity)
                                    .crop()
                                    .cropSquare()
                                    .compress(1024)
                                    .maxResultSize(1080,1080)
                                    .createIntent {
                                        startForProfileImageResult.launch(it)
                                    }
                            }
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    border= BorderStroke(1.dp, Color(parseColor("#f9f9f9"))),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        viewModel.setCurrentStep(4)
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