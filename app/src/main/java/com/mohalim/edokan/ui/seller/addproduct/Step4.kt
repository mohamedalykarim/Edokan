package com.mohalim.edokan.ui.seller.addproduct

import android.app.LocaleManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import android.net.Uri
import android.os.CountDownTimer
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohalim.edokan.R
import com.mohalim.edokan.core.model.Category
import com.mohalim.edokan.ui.main.DialogWithImage
import com.mohalim.edokan.ui.seller.SellerAddProductViewModel
import kotlinx.coroutines.flow.filter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Step4(
    context: Context,
    viewModel: SellerAddProductViewModel,
    startForProfileImageResult: ActivityResultLauncher<Intent>,
) {
    val chosenCategories by viewModel.chosenCategories.collectAsState()
    var showError by remember{ mutableStateOf(false) }

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

                AutocompleteTextFieldExample(viewModel)





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
                            onClick = {
                                viewModel.removeFromChosenCategories(category)
                            }) {

                            Row(modifier = Modifier.height(40.dp)) {

                                Column(modifier = Modifier
                                    .height(40.dp)
                                    .wrapContentHeight(Alignment.CenterVertically)) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)

                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = if (Locale.getDefault().language.equals("en")) category.categoryName else category.categoryNameAr,
                                    fontSize = 9.sp,
                                    color = Color.White,
                                    modifier = Modifier.clickable {
                                        viewModel.removeFromChosenCategories(category)
                                    })

                            }



                        }
                    }
                }



                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    border= BorderStroke(1.dp, Color(parseColor("#f9f9f9"))),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        if(viewModel.chosenCategories.value.isEmpty()){
                            showError = true
                            return@Button
                        }else{
                            viewModel.setCurrentStep(5)
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

        DialogWithImage(
            showDialog = showError,
            onDismissRequest = { showError = !showError },
            onConfirmation = { showError = !showError },
            painter = painterResource(id = R.drawable.error_icon) ,
            imageDescription = "Error",
            dialogText = "You should choose one category at least"
        )
    }
}




@Composable
fun AutocompleteTextFieldExample(
    viewModel: SellerAddProductViewModel,
) {
    // State variables for the input text and suggestions
    var text by remember { mutableStateOf("") }
    val isCategoriesSearchExpanded by viewModel.isCategoriesSearchExpanded.collectAsState(initial = false)
    val categories by viewModel.categories.collectAsState()


    val countDownTimer = object : CountDownTimer(2000,100) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            viewModel.searchForCategories(text)
        }

    }

    Column {

        TextField(
            value = text,
            onValueChange = {
                text = it
                countDownTimer.start()
                if(it == "" || (Calendar.getInstance().timeInMillis - viewModel.savedTime) < 1000){
                    countDownTimer.cancel()
                }

                viewModel.savedTime = Calendar.getInstance().timeInMillis
            },
            modifier = Modifier
                .padding(vertical = 15.dp, horizontal = 15.dp)
                .fillMaxWidth()
                .height(60.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            leadingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.ContentPasteSearch,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            placeholder = {
                Text(
                    text = "Search Category",
                    fontSize = 11.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                )
            },
            singleLine = true,
            textStyle = TextStyle.Default.copy(fontSize = 11.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(parseColor("#ffffff")),
                unfocusedContainerColor = Color(parseColor("#ffffff")),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            )
        )

        // Display dropdown menu with filtered suggestions
        DropdownMenu(
            expanded = isCategoriesSearchExpanded,
            onDismissRequest = { viewModel.setIsCategoriesSearchExpanded(false) },
        ) {
            categories.forEach { suggestion ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.addToChosenCategories(suggestion)
                        viewModel.setIsCategoriesSearchExpanded(false)
                        text = ""
                    }) {
                    Text(text = if (Locale.getDefault().language.equals("en")) suggestion.categoryName else suggestion.categoryNameAr)
                }
            }
        }
    }
}