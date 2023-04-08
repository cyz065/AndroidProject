package com.management.winwin.preference

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context:Context) {
    private val prefs : SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun getString(key:String, defaultValue: String): String {
        return prefs.getString(key, defaultValue).toString()
    }

    fun setString(key: String, value : String) {
        prefs.edit().putString(key, value).apply()
    }
}