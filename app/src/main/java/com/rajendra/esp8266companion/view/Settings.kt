package com.rajendra.esp8266companion.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rajendra.esp8266companion.viewmodel.SettingsViewmodel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SettingsScreen(viewmodel: SettingsViewmodel) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        item {
            SectionHeader("ESP 8266 Boot setting")

        }

        item {
            SettingsItem(title = "Identifier", description = viewmodel.identifier.value, onClick = {

                viewmodel.currenthint.value  = "Identifier"
                viewmodel.currentValue.value = viewmodel.identifier.value
                viewmodel.isShowDialog.value = true;
                viewmodel.position.value  = 0;

                /* Handle click */
            })
        }
        item {


            SettingsItem(title = "WIFI SSID", description = viewmodel.ssid.value, onClick = {
                viewmodel.currenthint.value  = "Wifi SSDI"
                viewmodel.currentValue.value = viewmodel.ssid.value
                viewmodel.isShowDialog.value = true;
                viewmodel.position.value  = 1;
            })
        }
        item {
            SettingsItem(
                title = "WIFI Password",
                description = viewmodel.ssidPassword.value,
                onClick = {
                /* Handle click */
                    viewmodel.currenthint.value  = "WIFI Password"

                    viewmodel.currentValue.value = viewmodel.ssidPassword.value
                    viewmodel.isShowDialog.value = true;
                    viewmodel.position.value  = 2;


                })
        }
        item {
            SettingsItem(
                title = "MQTT Server ",
                description = viewmodel.mqttServer.value,
                onClick = {
                /* Handle click */
                    viewmodel.currenthint.value  = "MQTT Server "

                    viewmodel.currentValue.value = viewmodel.mqttServer.value
                    viewmodel.isShowDialog.value = true;
                    viewmodel.position.value  = 3;


                })
        }
        item {
            SettingsItem(
                title = "MQTT Port",
                description = viewmodel.mqttPort.value,
                onClick = {
                /* Handle click */
                    viewmodel.currenthint.value  = "MQTT Port"

                    viewmodel.currentValue.value = viewmodel.mqttPort.value
                    viewmodel.isShowDialog.value = true;
                    viewmodel.position.value  = 4;

                })
        }
        item {
            SettingsItem(
                title = "MQTT Username",
                description = viewmodel.mqttUsername.value,
                onClick = {
                /* Handle click */
                    viewmodel.currenthint.value  = "MQTT Username"

                    viewmodel.currentValue.value = viewmodel.mqttUsername.value
                    viewmodel.isShowDialog.value = true;
                    viewmodel.position .value = 5;


                })
        }
        item {
            SettingsItem(
                title = "MQTT Password",
                description = viewmodel.mqttPassword.value,
                onClick = {
                /* Handle click */
                    viewmodel.currenthint.value  = "MQTT Password"

                    viewmodel.currentValue.value = viewmodel.mqttPassword.value
                    viewmodel.isShowDialog.value = true;
                    viewmodel.position.value  = 6;


                })
        }
        item {
            Divider()
        }

        item {

            SettingsItem("Connect","Click to Connect with ESP 8266", onClick = {
                viewmodel.navController.navigate("third")
            }) ;

        }

    }

    if (viewmodel.isShowDialog.value) {
        EditSettingDialog(
            hint  = viewmodel.currenthint.value,
            currentValue = viewmodel.currentValue.value,
            onValueChange = {

                viewmodel.currentValue.value = it
                viewmodel.updateValue();


                 },
            onDismiss = { viewmodel.isShowDialog.value = false },
            onConfirm = { viewmodel.isShowDialog.value = false }
        )
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun SettingsItem(title: String, description: String? = null, onClick: () -> Unit) {


    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        if (description != null) {
            Text(text = description, style = MaterialTheme.typography.titleSmall)
        }
    }
    Divider()
}

@Composable
fun SettingsToggle(title: String, checked: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)
        Switch(checked = checked.value, onCheckedChange = { checked.value = it })
    }
    Divider()



}

@Preview
@Composable
private fun ViewPre() {
    //SettingsScreen(viewmodel = SettingsViewmodel())

}

@Composable
fun EditSettingDialog(
    hint: String ="Edit Value",
    currentValue: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    var tempValue by remember { mutableStateOf(currentValue) }

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Updating") }, text = {
        TextField(
            value = tempValue,
            onValueChange = { tempValue = it },
            label = { Text(hint) })
    }, confirmButton = {
        TextButton(onClick = {
            onValueChange(tempValue)
            onConfirm()
        }) {
            Text("Confirm")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    })
}

