package com.example.urlreader.services

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.urlreader.data.RequestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class MyAccessibilityService() : AccessibilityService() {

    var text = ""
    var browserApp = ""
    var browserUrl = ""

    @Inject
    lateinit var repository: RequestRepository
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    @SuppressLint("SimpleDateFormat")
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val eventType: Int = event.eventType
        when (eventType) {
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

                // If this is not a supported browser, exit
                if (browserConfig == null) {
                    return
                }

                val capturedUrl: String? = captureUrl(parentNodeInfo, browserConfig)
                parentNodeInfo.recycle()

                if (capturedUrl == null) {
                    return
                }

                if (packageName != browserApp) {
                    if (android.util.Patterns.WEB_URL.matcher(capturedUrl).matches()) {
                        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
                        val currentDateTime = sdf.format(Date())
                        Log.d("AccessibilityService", "$packageName  or $capturedUrl or ${text} or ${currentDateTime}")
                        browserApp = packageName
                        browserUrl = capturedUrl
                    }
                } else {
                    if (capturedUrl != browserUrl) {
                        if (android.util.Patterns.WEB_URL.matcher(capturedUrl).matches()) {
                            browserUrl = capturedUrl

                            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
                            val currentDateTime = sdf.format(Date())
                            Log.d("AccessibilityService", "$packageName  and $capturedUrl and ${text} and ${currentDateTime}")
                        }
                    }
                }
            }
        }
    }

    private fun isBrowserPackage(packageName: String): Boolean {
        return packageName.startsWith("com.android.chrome") ||
                packageName.startsWith("org.mozilla.firefox") ||
                packageName.startsWith("com.opera.browser") ||
                packageName.startsWith("com.microsoft.emmx") ||
                packageName.startsWith("com.brave.browser") ||
                packageName.startsWith("com.duckduckgo.mobile.android")
    }

    private fun extractUrl(text: String): String? {
        // Use regular expressions or other parsing techniques to extract the URL from the text
        val urlRegex = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\((?:[^\\s()<>]+|(?:\\([^\\s()<>]+\\)))?\\))+(?:\\((?:[^\\s()<>]+|(?:\\([^\\s()<>]+\\)))?\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))"
        val matchResult = Regex(urlRegex).find(text)
        return matchResult?.value
    }

    private fun processRequestData(requestText: String, url: String) {
        // Process or store the request text and URL as needed
        Log.d("AccessibilityService", "Request Text: $requestText, URL: $url")
//        coroutineScope.launch {
//            try {
//                repository.insertRequest(Request(requestBody = requestText, url = url))
//            } catch (e: Exception) {
//                Log.e("AccessibilityService", "Error saving request data", e)
//            }
//        }
    }

    data class SupportedBrowserConfig(val packageName: String, val addressBarId: String)

    fun getSupportedBrowsers(): List<SupportedBrowserConfig> {
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


    fun convertEventTimeToDateTime(eventTimeMillis: Long): String {
        val eventDateTime = Date(eventTimeMillis)
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return format.format(eventDateTime)
    }

    override fun onInterrupt() {
        // Handle interruption
    }
}

