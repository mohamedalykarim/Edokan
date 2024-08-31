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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun Step1(
    context: Context,
    viewModel: SellerAddProductViewModel,
    startForProfileImageResult: ActivityResultLauncher<Intent>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        val holderUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(R.drawable.image_placeholder))
            .appendPath(context.resources.getResourceTypeName(R.drawable.image_placeholder))
            .appendPath(context.resources.getResourceEntryName(R.drawable.image_placeholder))
            .build()


        val imageUri by viewModel.imageUri.collectAsState(holderUri)

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
                    text = "Choose a thumb image for the product",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Row{
                    OutlinedButton(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .weight(1f),
                        border= BorderStroke(1.dp, Color(parseColor("#f6192a"))),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            ImagePicker
                                .with(context as AppCompatActivity)
                                .galleryOnly()
                                .crop()
                                .cropSquare()
                                .compress(1024)
                                .maxResultSize(1080,1080)
                                .createIntent {
                                    startForProfileImageResult.launch(it)
                                }
                        }, colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(parseColor("#f9f9f9"),
                            ))
                    ) {
                        Row {
                            Icon(
                                Icons.Default.Image,
                                modifier = Modifier.size(30.dp),
                                tint = Color(parseColor("#f6192a")),
                                contentDescription = "Gallery"
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Gallery", modifier = Modifier.padding(top = 3.dp), fontSize = 12.sp, color = Color(parseColor("#f6192a")))
                        }

                    }

                    OutlinedButton(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .weight(1f),
                        border= BorderStroke(1.dp, Color(parseColor("#f6192a"))),
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            ImagePicker
                                .with(context as AppCompatActivity)
                                .cameraOnly()
                                .crop()
                                .cropSquare()
                                .compress(1024)
                                .maxResultSize(1080,1080)
                                .createIntent {
                                    startForProfileImageResult.launch(it)
                                }
                        }, colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(parseColor("#f9f9f9"),
                            ))
                    ) {
                        Row {
                            Icon(
                                Icons.Default.Image,
                                modifier = Modifier.size(30.dp),
                                tint = Color(parseColor("#f6192a")),
                                contentDescription = "Gallery"
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Camera", modifier = Modifier.padding(top = 3.dp), fontSize = 12.sp, color = Color(parseColor("#f6192a")))
                        }

                    }

                }

                Spacer(modifier = Modifier.height(8.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .size(Size.ORIGINAL)
                        .build(),
                    placeholder = painterResource(id = R.drawable.image_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    border= BorderStroke(1.dp, Color(parseColor("#f9f9f9"))),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        viewModel.setCurrentStep(2)
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