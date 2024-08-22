package com.mohalim.edokan.ui.main

import android.content.Context
import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mohalim.edokan.R

@Composable
fun SplashScreen(context: Context, viewModel: LoginViewModel){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(parseColor("#f6192a"))),
        contentAlignment = Alignment.Center
    ){
        Image(painterResource(id =  R.drawable.transparent_bg ), modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, contentDescription = "Background")
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
            Image(painterResource(id =  R.drawable.logo ), modifier = Modifier
                .padding(start = 50.dp, end = 50.dp),
                contentScale = ContentScale.FillWidth,
                contentDescription = "Logo")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(50.dp).width(30.dp))
            }

        }

    }
}