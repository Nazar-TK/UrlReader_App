package com.example.urlreader.services

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.urlreader.data.Request
import com.example.urlreader.data.RequestRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject


@AndroidEntryPoint
class MyAccessibilityService : AccessibilityService() {

    private val TAG = "MyAccessibilityService"

    private var text = "Nothing was typed."
    private var browserApp = ""
    private var browserUrl = ""

    @Inject
    lateinit var repository: RequestRepository
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    @SuppressLint("SimpleDateFormat")
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            // save user Request text
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {

                event.text.let{ text = it.toString()}
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED, AccessibilityEvent.TYPE_WINDOWS_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                val parentNodeInfo: AccessibilityNodeInfo? = event.source
                if (parentNodeInfo == null) {
                    return
                }
                val packageName: String = event.packageName.toString()

                val supportedBrowsers = getSupportedBrowsers()
                var browserConfig: SupportedBrowserConfig? = null
                for (supportedConfig in supportedBrowsers) {
                    if (supportedConfig.packageName == packageName) {
                        browserConfig = supportedConfig
                        break
                    }
                }

                // If it is not a supported browser than exit
                if (browserConfig == null) {
                    return
                }

                val capturedUrl: String? = captureUrl(parentNodeInfo, browserConfig)
                parentNodeInfo.recycle()

                if (capturedUrl == null) {
                    return
                }

                // Save only those requests, where url contains "google.com"
                if (packageName != browserApp) {
                    if (android.util.Patterns.WEB_URL.matcher(capturedUrl).matches() && capturedUrl.contains("google.com")) {
                        browserApp = packageName
                        browserUrl = capturedUrl
                        // remove braces from text
                        text = text.replace("[\\[\\]]".toRegex(), "")
                        processRequestData(requestText = text, url = capturedUrl, LocalDateTime.now())
                    }
                } else {
                    if (capturedUrl != browserUrl && capturedUrl.contains("google.com")) {
                        if (android.util.Patterns.WEB_URL.matcher(capturedUrl).matches()) {
                            browserUrl = capturedUrl
                            // remove braces from text
                            text = text.replace("[\\[\\]]".toRegex(), "")
                            processRequestData(requestText = text, url = capturedUrl, LocalDateTime.now())
                        }
                    }
                }
            }
        }
    }


    private fun processRequestData(requestText: String, url: String, dateTime: LocalDateTime) {
        // Process or store the request text and URL as needed

        coroutineScope.launch {
            try {
                repository.insertRequest(Request(requestBody = requestText, url = url, date = dateTime))
                Log.d(TAG, "Saved $requestText and  $url and $dateTime")
            } catch (e: Exception) {
                Log.e(TAG, "Error saving request data", e)
            }
        }
    }

    data class SupportedBrowserConfig(val packageName: String, val addressBarId: String)

    private fun getSupportedBrowsers(): List<SupportedBrowserConfig> {
        val browsers = mutableListOf<SupportedBrowserConfig>()
        browsers.add(SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"))
        browsers.add(SupportedBrowserConfig("org.mozilla.firefox", "org.mozilla.firefox:id/mozac_browser_toolbar_url_view"))
        browsers.add(SupportedBrowserConfig("com.opera.browser", "com.opera.browser:id/url_field"))
        browsers.add(SupportedBrowserConfig("com.opera.mini.native", "com.opera.mini.native:id/url_field"))
        browsers.add(SupportedBrowserConfig("com.duckduckgo.mobile.android", "com.duckduckgo.mobile.android:id/omnibarTextInput"))
        browsers.add(SupportedBrowserConfig("com.microsoft.emmx", "com.microsoft.emmx:id/url_bar"))
        return browsers
    }

    private fun captureUrl(info: AccessibilityNodeInfo?, config: SupportedBrowserConfig): String? {
        info ?: return null
        val nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId)
        if (nodes.isNullOrEmpty()) {
            return null
        }
        val addressBarNodeInfo = nodes[0]
        val url = addressBarNodeInfo.text?.toString()
        return url
    }

    override fun onInterrupt() {
        // Handle interruption
    }
}

