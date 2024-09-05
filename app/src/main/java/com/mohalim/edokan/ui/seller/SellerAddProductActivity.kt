package com.mohalim.edokan.ui.seller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.dhaval2404.imagepicker.ImagePicker
import com.maryamrzdh.stepper.Stepper
import com.mohalim.edokan.ui.seller.addproduct.Step1
import com.mohalim.edokan.ui.seller.addproduct.Step2
import com.mohalim.edokan.ui.seller.addproduct.Step3
import com.mohalim.edokan.ui.seller.addproduct.Step4
import com.mohalim.edokan.ui.seller.addproduct.Step5
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerAddProductActivity : AppCompatActivity() {
    val viewModel: SellerAddProductViewModel by viewModels()


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {

                data?.let {
                    Log.d("TAG", "onActivityResult: "+data.getStringExtra("PROCESS"))
                    val process = data.getStringExtra("PROCESS")
                    Log.d("TAG", "onActivityResult: "+process)
                    val fileUri = data.data!!


                    if (viewModel.imageProcess.value == "THUMBNAIL"){
                        viewModel.setImageUri(fileUri)
                    }else if (viewModel.imageProcess.value == "IMAGE_1"){
                        viewModel.setImage1Uri(fileUri)
                    }else if (viewModel.imageProcess.value == "IMAGE_2"){
                        viewModel.setImage2Uri(fileUri)
                    }else if (viewModel.imageProcess.value == "IMAGE_3"){
                        viewModel.setImage3Uri(fileUri)
                    }else if (viewModel.imageProcess.value == "IMAGE_4"){
                        viewModel.setImage4Uri(fileUri)
                    }else{

                    }
                }




            } else if (resultCode == ImagePicker.RESULT_ERROR) {
            } else {
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SellerAddProductScreen(this, viewModel, startForProfileImageResult)
        }
    }

    override fun onResume() {
        super.onResume()

    }
}

@Composable
fun SellerAddProductScreen(
    context: Context,
    viewModel: SellerAddProductViewModel,
    startForProfileImageResult: ActivityResultLauncher<Intent>,
) {
    val formState by viewModel.formState.collectAsState()
    val currentStep by viewModel.currentStep.collectAsState()

    val numberStep = 5
    val titleList= arrayListOf("Image","Basics","Details","Category", "Review")


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
                startForProfileImageResult)
        }else if (currentStep == 2) {
            Step2(
                context,
                viewModel,
                startForProfileImageResult)
        }else if (currentStep == 3) {
            Step3(
                context,
                viewModel,
                startForProfileImageResult)
        }else if (currentStep == 4) {
            Step4(
                context,
                viewModel,
                startForProfileImageResult)
        }else if (currentStep == 5) {
            Step5(
                context,
                viewModel,
                startForProfileImageResult)
        }
}


}