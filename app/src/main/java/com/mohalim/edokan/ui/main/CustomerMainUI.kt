package com.mohalim.edokan.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.mohalim.edokan.core.utils.AuthUtils
import kotlinx.coroutines.launch

@Composable
fun CustomerMainUI(viewModel: MainViewModel) {
    val username by viewModel.username.collectAsState(initial = "")
    val phoneNumber by viewModel.phoneNumber.collectAsState(initial = "")
    val imageUrl by viewModel.imageUrl.collectAsState(initial = "")

    val coroutineScope = rememberCoroutineScope()


    Column {
        Text("Username: Customer $username")

        Button(onClick = {
            AuthUtils.signOut(viewModel.auth)
            coroutineScope.launch {
                viewModel.userPreferencesRepository.clearUserDetails()
                viewModel.userSelectionPreferencesRepository.clearSelectedMarketplace()
            }
        }) {
            Text(text = "Sign Out")
        }

    }
}