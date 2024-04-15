package com.example.urlreader.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings

object AccessibilityUtils {

    fun isAccessibilityPermissionOn(context: Context): Boolean {
        var accessEnabled = 0
        try {
            accessEnabled = Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return (accessEnabled == 1)
    }

    fun openAccessibilitySettings(context: Context) {
        // if not construct intent to request permission
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        // request permission via start activity for result
        context.startActivity(intent)
    }
}
