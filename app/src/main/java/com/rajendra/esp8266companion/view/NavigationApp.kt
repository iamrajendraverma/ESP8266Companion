package com.rajendra.esp8266companion.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.rajendra.esp8266companion.R
import com.rajendra.esp8266companion.viewmodel.ConnectionViewModel
import com.rajendra.esp8266companion.viewmodel.SettingsViewmodel

@Composable
fun NavigationApp(activity: ComponentActivity) {
    val navController = rememberNavController()
    val activity = activity;

    NavHost(navController = navController, startDestination = "first") {
        composable("first") { APScreen(navController) }
        composable("second") { ESPScreen(activity, navController) }
        composable(route = "third") {
            ConnectionScreen(activity, navController)


        }
        // Add more destinations here
    }
}

@Composable
fun ConnectionScreen(activity: ComponentActivity, navController: NavController) {
    val connectionViewModel: ConnectionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            val context = LocalContext.current
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ConnectionViewModel(context, navController) as T
            }

        }

    )
    ConnectionView(connectionViewModel)
}

@Composable
fun APScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(top=200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = "This is MCU node",
        )
        Button(onClick = { navController.navigate("second") }) {
            Text(text = stringResource(R.string.app_name))
        }
    }
}

@Composable
fun ESPScreen(activity: ComponentActivity, navController: NavController) {
    val settingsViewmodel: SettingsViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            val context = LocalContext.current
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewmodel(context, navController) as T
            }

        }

    )
    SettingsScreen(viewmodel = settingsViewmodel)
}