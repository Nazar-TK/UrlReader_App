package com.example.urlreader.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.urlreader.presentation.RequestList.RequestListScreen
import com.example.urlreader.presentation.ui.theme.UrlReaderTheme
import com.example.urlreader.utils.AccessibilityUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!AccessibilityUtils.isAccessibilityPermissionOn(this)) {
            AccessibilityUtils.openAccessibilitySettings(this)
        }

        setContent {
            UrlReaderTheme {
                // A surface container using the 'background' color from the theme
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