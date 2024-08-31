package com.mohalim.edokan.ui.general

import android.graphics.Color.parseColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohalim.edokan.core.model.Step

@Composable
fun StepIndicator(
    step: Step,
    isCurrentStep: Boolean,
    stepNumber: Int
) {
    val circleColor = when {
        step.isCompleted -> Color(parseColor("#43A047"))
        isCurrentStep -> Color(parseColor("#f6192a"))
        else -> Color.Gray
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Canvas(modifier = Modifier.size(15.dp)) {
            drawCircle(color = circleColor)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = stepNumber.toString(), color = circleColor, fontSize = 11.sp)
    }
}