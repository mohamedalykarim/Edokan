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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step4(
    context: Context,
    viewModel: SellerAddProductViewModel,
    startForProfileImageResult: ActivityResultLauncher<Intent>,
) {
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


                val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
                var exp by remember { mutableStateOf(false) }
                var searchText by remember { mutableStateOf("") }

                Column {

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = searchText,
                        label = {
                            Text("Category", fontSize = 12.sp)
                        },
                        maxLines = 1,
                        onValueChange = {
                            searchText = it
                            options.filter { it.contains(searchText) }

                            viewModel.searchForCategories(it)
                        })

                    Column {
                        var i = 0
                        options.forEach {
                            var topMargin = 3.dp
                            if (i == 0) {
                                topMargin = 0.dp
                            }
                            Column(modifier = Modifier
                                .padding(start = 4.dp, top = topMargin)
                                .fillMaxWidth()
                                .background(color = Color.White)
                                .padding(10.dp)) {
                                Text(text = ""+it, fontSize = 11.sp)
                            }
                            i++
                        }
                    }
                }



                FlowRow() {
                    Button(modifier = Modifier.padding(start = 4.dp), shape = RoundedCornerShape(10.dp), onClick = { /*TODO*/ }) {
                        Text(text = "Supermarket", fontSize = 10.sp)
                    }

                    Button(modifier = Modifier.padding(start = 4.dp),shape = RoundedCornerShape(10.dp), onClick = { /*TODO*/ }) {
                        Text(text = "Vegetables", fontSize = 11.sp)
                    }

                    Button(modifier = Modifier.padding(start = 4.dp),shape = RoundedCornerShape(10.dp), onClick = { /*TODO*/ }) {
                        Text(text = "Herbalist", fontSize = 11.sp)
                    }
                    Button(modifier = Modifier.padding(start = 4.dp),shape = RoundedCornerShape(10.dp), onClick = { /*TODO*/ }) {
                        Text(text = "Pharmacy", fontSize = 11.sp)
                    }
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