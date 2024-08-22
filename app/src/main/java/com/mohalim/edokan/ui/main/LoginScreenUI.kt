package com.mohalim.edokan.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohalim.edokan.R


@Composable
fun LoginScreen(viewModel: LoginViewModel){
    val uiState by viewModel.uiState.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize(), // Additional padding inside the content
        contentAlignment = Alignment.Center
    ) {
        Image(painterResource(id =  R.drawable.transparent_bg ), modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, contentDescription = "Background")

        Column(
            modifier = Modifier
                .padding(16.dp).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = "Welcome to E-Dokan", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)

            Spacer(modifier = Modifier.height(32.dp))

            if (uiState is VerificationState.Initial || uiState is VerificationState.LoadingInitial) {
                Row {
                    TextField(
                        value = "+20",
                        onValueChange = {  },
                        label = { Text("Egypt") },
                        enabled = false,
                        modifier = Modifier.width(100.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))


                    TextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.sendVerificationCode(phoneNumber) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = (uiState is VerificationState.Initial && phoneNumber.length == 10) || (uiState is VerificationState.Initial && phoneNumber.length == 11)

                ) {
                    Text("Send OTP")
                }

                if (uiState is VerificationState.LoadingInitial){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(50.dp).width(30.dp))
                    }
                }
            }

            if (uiState is VerificationState.CodeSent || uiState is VerificationState.VerificationFailed || uiState is VerificationState.LoadingCodeSent) {
                TextField(
                    value = otpCode,
                    onValueChange = { otpCode = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text("Enter OTP") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.verifyCode(otpCode) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState is VerificationState.CodeSent && otpCode.length == 6
                ) {
                    Text("Verify OTP")
                }

                if (uiState is VerificationState.LoadingCodeSent){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(50.dp).width(30.dp))
                    }
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it)
            }
        }


    }
}
