package com.rajendra.esp8266companion.viewmodel

import android.content.Context
import android.net.wifi.WifiManager
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.rajendra.esp8266companion.storage.SharedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class ConnectionViewModel (context: Context, navController: NavController):ViewModel() {

    var sharedPrefsManager  = SharedPrefsManager(context)
    var identifier = mutableStateOf(sharedPrefsManager.getString("identifier","rajendra"))
    var ssid = mutableStateOf(sharedPrefsManager.getString("ssid","rajendra-4g"))
    var ssidPassword = mutableStateOf(sharedPrefsManager.getString("ssidPassword","root1234"))
    var mqttServer = mutableStateOf(sharedPrefsManager.getString("mqttServer","192.168.29.135"))
    var mqttPort = mutableStateOf(sharedPrefsManager.getString("mqttPort","1883"))
    var mqttUsername = mutableStateOf(sharedPrefsManager.getString("mqttUsername","admin"))
    var mqttPassword = mutableStateOf(sharedPrefsManager.getString("mqttPassword","root1234"))

    val wifiManager = context.applicationContext.getSystemService(ComponentActivity.WIFI_SERVICE) as WifiManager
    val wifiConfig = android.net.wifi.WifiConfiguration().apply {
        SSID = "\"ESP8266-Config\""
        preSharedKey = "\"password\"" // Use the AP password
    }
    var feedback  = mutableStateOf("")

    fun connectToNode() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val netId = wifiManager.addNetwork(wifiConfig)
                wifiManager.disconnect()
                wifiManager.enableNetwork(netId, true)
                wifiManager.reconnect()
                val data = """
                                {   "identifier":"${identifier.value}",
                                    "wifiSSID": "${ssid.value}",
                                    "wifiPassword": "${ssidPassword.value}",
                                    "mqttServer": "${mqttServer.value}",
                                    "mqttPort": "${mqttPort.value}",
                                    "mqttUser": "${mqttUsername.value}",
                                    "mqttPassword": "${mqttPassword.value}"
                                }
                            """.trimIndent()

                println("data: $data")
                withContext(Dispatchers.Main) {
                    feedback.value = "Connecting to ESP8266 WiFi..."
                }
                delay(5000) // wait for wifi connection
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder().url("ws://192.168.4.1:81").build()
                 var    webSocket = client.newWebSocket(request, object : WebSocketListener() {
                        override fun onOpen(webSocket: WebSocket, response: Response) {
                            viewModelScope.launch(Dispatchers.Main) {


                                feedback.value = "WebSocket connected"

                                webSocket.send(data)
                            }
                        }

                        override fun onMessage(webSocket: WebSocket, text: String) {
                            viewModelScope.launch(Dispatchers.Main) {
                                feedback.value = text
                            }
                        }

                        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                            viewModelScope.launch(Dispatchers.Main) {
                                feedback.value = "Received bytes: ${bytes.hex()}"
                            }
                        }

                        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                            webSocket.close(1000, null)
                            viewModelScope.launch(Dispatchers.Main) {
                                feedback.value = "Closing WebSocket: $code / $reason"
                            }
                        }

                        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                            viewModelScope.launch(Dispatchers.Main) {
                                feedback.value = "WebSocket closed: $code / $reason"
                            }
                        }

                        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                            viewModelScope.launch(Dispatchers.Main) {
                              //  feedback.value = "WebSocket Error: ${t.message}"
                            }
                        }
                    })
                } catch (e: Exception) {
                    viewModelScope.launch(Dispatchers.Main) {
//                        feedback.value = "WebSocket Error: ${e.message}"
                    }
                }
            }
        }
    }

}