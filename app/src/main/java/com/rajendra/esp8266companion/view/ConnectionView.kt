package com.rajendra.esp8266companion.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.rajendra.esp8266companion.R
import com.rajendra.esp8266companion.viewmodel.ConnectionViewModel


@Composable
fun ConnectionView(viewModel: ConnectionViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = "This is MCU node",
        )

        Button(onClick = {

            viewModel.connectToNode();
        }) {

            Text("Connect to Cloud")
        }
        Text(viewModel.feedback.value)


    }

}

