package com.rajendra.esp8266companion.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.rajendra.esp8266companion.storage.SharedPrefsManager

class SettingsViewmodel(context: Context, navController:NavController) : ViewModel(){
    var sharedPrefsManager = SharedPrefsManager(context)
    var navController  =navController;

    var position = mutableStateOf(0);
    var identifier = mutableStateOf(sharedPrefsManager.getString("identifier","rajendra"))
    var ssid = mutableStateOf(sharedPrefsManager.getString("ssid","rajendra-4g"))
    var ssidPassword = mutableStateOf(sharedPrefsManager.getString("ssidPassword","root1234"))
    var mqttServer = mutableStateOf(sharedPrefsManager.getString("mqttServer","192.168.29.135"))
    var mqttPort = mutableStateOf(sharedPrefsManager.getString("mqttPort","1883"))
    var mqttUsername = mutableStateOf(sharedPrefsManager.getString("mqttUsername","admin"))
    var mqttPassword = mutableStateOf(sharedPrefsManager.getString("mqttPassword","root1234"))
    var currentValue = mutableStateOf("")
    var currenthint = mutableStateOf("")
    var isShowDialog = mutableStateOf(false)

    fun updateValue() {
        val position = position.value
        when (position) {
            0 -> {identifier.value = currentValue.value
            sharedPrefsManager.saveString("identifier",identifier.value)
            }
            1 -> {ssid.value = currentValue.value
                sharedPrefsManager.saveString("ssid",ssid.value)
            }
            2 -> {ssidPassword.value = currentValue.value
            sharedPrefsManager.saveString("ssidPassword", ssidPassword.value)
            }
            3 -> {mqttServer.value = currentValue.value
            sharedPrefsManager.saveString("mqttServer", mqttServer.value)
            }
            4 ->{ mqttPort.value = currentValue.value
            sharedPrefsManager.saveString("mqttPort",mqttPort.value)
            }
            5 -> mqttUsername.value = currentValue.value
            6 -> mqttPassword.value = currentValue.value
            else -> println("Something went wrong") // Default case

        }
    }
}

