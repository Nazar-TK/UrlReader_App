package com.example.urlreader.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.urlreader.presentation.RequestList.RequestListScreen
import com.example.urlreader.presentation.ui.theme.UrlReaderTheme
import com.example.urlreader.utils.AccessibilityUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!AccessibilityUtils.isAccessibilityPermissionOn(this)) {
            AccessibilityUtils.openAccessibilitySettings(this)
            Log.d(TAG, "Accessibility Permission disabled")
        }

        setContent {
            UrlReaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestListScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!AccessibilityUtils.isAccessibilityPermissionOn(this)) {
            AccessibilityUtils.openAccessibilitySettings(this)
        }
    }
}