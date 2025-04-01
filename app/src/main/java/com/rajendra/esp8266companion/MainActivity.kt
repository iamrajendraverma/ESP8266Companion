package com.rajendra.esp8266companion

import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rajendra.esp8266companion.view.NavigationApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okio.ByteString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //ConfigScreen()
            var viewModelStore = this;
            NavigationApp(viewModelStore)
        }
    }
}

@Composable
fun ConfigScreen() {
    val context = LocalContext.current
    var wifiSSID by remember { mutableStateOf("") }
    var wifiPassword by remember { mutableStateOf("") }
    var mqttServer by remember { mutableStateOf("") }
    var mqttPort by remember { mutableStateOf("") }
    var mqttUser by remember { mutableStateOf("") }
    var mqttPassword by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var webSocket: WebSocket? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = wifiSSID, onValueChange = { wifiSSID = it }, label = { Text("WiFi SSID") })
        TextField(value = wifiPassword, onValueChange = { wifiPassword = it }, label = { Text("WiFi Password") })
        TextField(value = mqttServer, onValueChange = { mqttServer = it }, label = { Text("MQTT Server") })
        TextField(value = mqttPort, onValueChange = { mqttPort = it }, label = { Text("MQTT Port") })
        TextField(value = mqttUser, onValueChange = { mqttUser = it }, label = { Text("MQTT User") })
        TextField(value = mqttPassword, onValueChange = { mqttPassword = it }, label = { Text("MQTT Password") })

        Button(onClick = {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    val wifiManager = context.applicationContext.getSystemService(ComponentActivity.WIFI_SERVICE) as WifiManager
                    val wifiConfig = android.net.wifi.WifiConfiguration().apply {
                        SSID = "\"ESP8266-Config\""
                        preSharedKey = "\"password\"" // Use the AP password
                    }

                    val netId = wifiManager.addNetwork(wifiConfig)
                    wifiManager.disconnect()
                    wifiManager.enableNetwork(netId, true)
                    wifiManager.reconnect()
                    withContext(Dispatchers.Main) {
                        feedback = "Connecting to ESP8266 WiFi..."
                    }
                    delay(5000) // wait for wifi connection

                    try {
                        val client = OkHttpClient()
                        val request = Request.Builder().url("ws://192.168.4.1:81").build()
                        webSocket = client.newWebSocket(request, object : WebSocketListener() {
                            override fun onOpen(webSocket: WebSocket, response: Response) {
                                coroutineScope.launch(Dispatchers.Main) {


                                    feedback = "WebSocket connected"
                                    val json = """
                                {
                                    "wifiSSID": "$wifiSSID",
                                    "wifiPassword": "$wifiPassword",
                                    "mqttServer": "$mqttServer",
                                    "mqttPort": "$mqttPort",
                                    "mqttUser": "$mqttUser",
                                    "mqttPassword": "$mqttPassword"
                                }
                            """.trimIndent()
                                    webSocket.send(json)
                                }
                            }

                            override fun onMessage(webSocket: WebSocket, text: String) {
                                coroutineScope.launch(Dispatchers.Main) {
                                    feedback = text
                                }
                            }

                            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                                coroutineScope.launch(Dispatchers.Main) {
                                    feedback = "Received bytes: ${bytes.hex()}"
                                }
                            }

                            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                                webSocket.close(1000, null)
                                coroutineScope.launch(Dispatchers.Main) {
                                    feedback = "Closing WebSocket: $code / $reason"
                                }
                            }

                            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                                coroutineScope.launch(Dispatchers.Main) {
                                    feedback = "WebSocket closed: $code / $reason"
                                }
                            }

                            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                                coroutineScope.launch(Dispatchers.Main) {
                                    feedback = "WebSocket Error: ${t.message}"
                                }
                            }
                        })
                    } catch (e: Exception) {
                        coroutineScope.launch(Dispatchers.Main) {
                            feedback = "WebSocket Error: ${e.message}"
                        }
                    }
                }
            }
        }) {
            Text("Submit")
        }

        Text(feedback)
    }
}