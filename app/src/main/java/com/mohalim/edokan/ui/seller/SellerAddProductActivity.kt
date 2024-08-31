package com.mohalim.edokan.ui.seller

import android.content.Context
import android.graphics.Color.parseColor
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.firebase.storage.FirebaseStorage
import com.maryamrzdh.stepper.Stepper
import com.mohalim.edokan.core.model.Step
import com.mohalim.edokan.ui.seller.addproduct.Step1
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect

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
    val currentStep by viewModel.currentStep.collectAsState()

    val numberStep = 4
    val titleList= arrayListOf("Image","Basics","Details","Review")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        Stepper(
            numberOfSteps = numberStep,
            currentStep = currentStep,
            stepDescriptionList = titleList,
            unSelectedColor = Color(parseColor(/* colorString = */ "#FAE7E9")),
            selectedColor = Color(parseColor("#f6192a")),
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (currentStep == 1) {
            Step1(
                context,
                viewModel,
                onNext = { viewModel.setCurrentStep(2) })
        }
}


}