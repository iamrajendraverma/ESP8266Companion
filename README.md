## ESP8266 Companion App: Configuration and Switch Logging

The ESP8266 Companion app is an Android utility designed to streamline the configuration of the [ESPClient](https://github.com/iamrajendraverma/Esp8266Client) firmware, enabling seamless operation of the ESP8266 as a Wi-Fi button with Node-RED integration.

**Prerequisites:**

1.  **ESPClient Firmware:** Ensure the ESP8266 microcontroller is flashed with the ESPClient firmware.
2.  **Node-RED:** Node-RED should be configured to receive and process data from the ESP8266.

**Configuration Procedure:**

1.  **Enable Access Point Mode:** Power on the ESP8266 and press the flash button to activate access point mode. The ESP8266 will broadcast a Wi-Fi network named "ESP8266-Config" with the password "password".
2.  **Connect to ESP8266 Wi-Fi:** On your Android device, connect to the "ESP8266-Config" Wi-Fi network.
3.  **Launch the ESP8266 Companion App:** Open the app and follow the on-screen instructions, as depicted in the following image:

    ![ESP8266 Client Configuration](https://github.com/user-attachments/assets/c0798bbc-92d0-4daa-84cb-6a616c3a5fe6)

4.  **Connect to Cloud:** Tap the "Connect to Cloud" button within the app. Upon successful configuration, a "Config received" message will be displayed, indicating that the ESP8266 client is connected to your local Wi-Fi network.

**Switch Logging and Data Recording:**

The ESP8266 Companion app, in conjunction with Node-RED, allows you to record and track switch activity. This feature enables comprehensive logging and analysis of device interactions.

**Visual Representation:**

<div style="display: flex; flex-direction: row;">
  <img src="https://github.com/user-attachments/assets/28fa407b-05f1-42e5-af0e-eb15d27cb250" style="width: 50%; height: auto; margin-right: 10px;">
  <img src="https://github.com/user-attachments/assets/8d3160d0-45bb-45d7-86bc-d31347533204" style="width: 50%; height: auto;">
</div>
