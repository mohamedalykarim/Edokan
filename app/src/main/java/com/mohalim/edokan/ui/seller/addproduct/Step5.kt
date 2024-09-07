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
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.Alignment
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
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step5(
    context: Context,
    viewModel: SellerAddProductViewModel,
    startForProfileImageResult: ActivityResultLauncher<Intent>,
) {
    val scrollState = rememberScrollState()

    val holderUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(context.resources.getResourcePackageName(R.drawable.image_placeholder))
        .appendPath(context.resources.getResourceTypeName(R.drawable.image_placeholder))
        .appendPath(context.resources.getResourceEntryName(R.drawable.image_placeholder))
        .build()

    val productName by viewModel.productName.collectAsState()
    val productDescription by viewModel.productDescription.collectAsState()
    val productPrice by viewModel.productPrice.collectAsState()
    val productQuantity by viewModel.productQuantity.collectAsState()

    val productWidth by viewModel.productWidth.collectAsState()
    val productHeight by viewModel.productHeight.collectAsState()
    val productWeight by viewModel.productWeight.collectAsState()
    val productLength by viewModel.productLength.collectAsState()
    val productDiscount by viewModel.productDiscount.collectAsState()

    val chosenCategories by viewModel.chosenCategories.collectAsState()


    val imageUri by viewModel.imageUri.collectAsState(holderUri)
    val image1Uri by viewModel.image1Uri.collectAsState(holderUri)
    val image2Uri by viewModel.image2Uri.collectAsState(holderUri)
    val image3Uri by viewModel.image3Uri.collectAsState(holderUri)
    val image4Uri by viewModel.image4Uri.collectAsState(holderUri)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(state = scrollState)
    ) {

        /**
         * Basics Card
         */

        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(parseColor("#f5f5f5"))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                Text(
                    text = "Basics: ",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Product Name: ",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = productName  ,
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = "Product Description: ",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = productDescription ,
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Product Price: ",
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = productPrice.toString(),
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Product Quantity: ",
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = productQuantity.toString() ,
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        /**
         * Details Card
         */
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(parseColor("#f5f5f5"))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                Text(
                    text = "Details: ",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Product Width: ",
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "" + productWidth  ,
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Product Height: ",
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = productHeight.toString() ,
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Product Weight: ",
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = productWeight.toString(),
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Product Length: ",
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = productLength.toString() ,
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Product Discount: ",
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = productDiscount.toString() ,
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /**
         * Categories Card
         */
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(parseColor("#f5f5f5"))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                Text(
                    text = "Selected Categories: ",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))


                FlowRow() {
                    chosenCategories.forEach {category->
                        Button(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(parseColor("#f6192a"),
                                )),
                            onClick = {}) {

                            Text(
                                text = if (Locale.getDefault().language.equals("en")) category.categoryName else category.categoryNameAr,
                                fontSize = 9.sp,
                                color = Color.White
                            )



                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.elevatedCardColors(
                containerColor = Color(parseColor("#f5f5f5"))
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                Text(
                    text = "Images",
                    modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .size(Size.ORIGINAL)
                        .build(),
                    placeholder = painterResource(id = R.drawable.image_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                )

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
                    )
                }

                Row {
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
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 4.dp),
            border= BorderStroke(1.dp, Color(parseColor("#f9f9f9"))),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                viewModel.addNewProduct()
            }, colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(parseColor("#f6192a"),
                ))
        ) {
            Row {
                Text(
                    text = "Add New Product",
                    modifier = Modifier.padding(top = 3.dp),
                    fontSize = 12.sp,
                    color = Color(parseColor("#f9f9f9"))
                )
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