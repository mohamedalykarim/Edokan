package com.mohalim.edokan.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun CustomerMainUI(viewModel: MainViewModel) {
    val username by viewModel.username.collectAsState(initial = "")
    val phoneNumber by viewModel.phoneNumber.collectAsState(initial = "")
    val imageUrl by viewModel.imageUrl.collectAsState(initial = "")

    Column {
        Text("Username: Customer $username")

    }
}