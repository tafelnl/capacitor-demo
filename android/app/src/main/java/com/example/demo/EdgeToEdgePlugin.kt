package com.example.demo

import android.view.ViewGroup.MarginLayoutParams
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.webkit.WebViewCompat
import com.getcapacitor.Plugin
import com.getcapacitor.annotation.CapacitorPlugin
import java.util.regex.Pattern

@CapacitorPlugin(name = "EdgeToEdge")
class EdgeToEdgePlugin : Plugin() {
    fun isMinimumWebViewVersionRequired(): Boolean {
        val info = WebViewCompat.getCurrentWebViewPackage(context)

        if (info != null) {
            val pattern = Pattern.compile("(\\d+)")
            val matcher = pattern.matcher(info.versionName)
            if (matcher.find()) {
                val majorVersionStr = matcher.group(0)
                val majorVersion = majorVersionStr?.toInt()
                if (majorVersion != null) {
                    return majorVersion >= 140
                }
            }
        }

        return false
    }

    override fun load() {
        // @TODO: this should be re-run upon page (re)load
        val that = this
        activity.runOnUiThread {
            this.bridge.activity.window.decorView.let { view ->
                ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
                    val webViewSupportsSafeAreaInsetsCorrectly = isMinimumWebViewVersionRequired()

                    if (webViewSupportsSafeAreaInsetsCorrectly) {
                        that.bridge.webView.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();") {
                            // @TODO: make checking for `viewport-fit=cover` meta tag more robust
                            val userOptedInForHandlingSafeAreaInsets = it.contains("viewport-fit=cover")
                            if (userOptedInForHandlingSafeAreaInsets) {
                                // Developer should handle safe area insets in css
                                // @TODO: this cannot yet be returned here, as the call to `evaluateJavascript` is async with a callback
                                windowInsets
                            }
                        }
                        // @TODO: we should actually not return `windowInsets` here yet
                        // but we have to because - as explained above - `evaluateJavascript` is async
                        return@setOnApplyWindowInsetsListener windowInsets
                    }

                    // Add a margin so that the webview still will be shown within the safe area insets,
                    // without the developer having to do anything
                    val v = that.bridge.webView
                    val insets: Insets = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
                    )
                    val mlp = v.layoutParams as MarginLayoutParams
                    mlp.leftMargin = insets.left
                    mlp.bottomMargin = insets.bottom
                    mlp.rightMargin = insets.right
                    mlp.topMargin = insets.top
                    v.layoutParams = mlp
                    WindowInsetsCompat.CONSUMED
                }
            }
        }
    }
}
