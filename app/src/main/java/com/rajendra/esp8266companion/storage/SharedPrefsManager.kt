package com.rajendra.esp8266companion.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefsManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        sharedPreferences.edit() { putString(key, value) }
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit() { putInt(key, value) }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit() { putBoolean(key, value) }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun clear() {
        sharedPreferences.edit() { clear() }
    }

    fun remove(key:String){
        sharedPreferences.edit() { remove(key) }
    }
}