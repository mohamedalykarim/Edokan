package com.mohalim.edokan.ui.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohalim.edokan.core.model.Step

@Composable
fun VerticalStepProgressBar(
    steps: List<Step>,
    currentStepIndex: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Step Indicator
                StepIndicator(
                    step = step,
                    isCurrentStep = index == currentStepIndex,
                    stepNumber = index + 1
                )

                // Step Title
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = step.title,
                    color = if (step.isCompleted || index == currentStepIndex) Color.Black else Color.Gray,
                    modifier = Modifier.weight(1f),
                    fontSize = 11.sp
                )
            }

            // Connector between steps
            if (index < steps.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .width(2.dp)
                        .height(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}