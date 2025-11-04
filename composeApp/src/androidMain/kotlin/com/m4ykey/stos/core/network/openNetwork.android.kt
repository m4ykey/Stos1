package com.m4ykey.stos.core.network

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.m4ykey.stos.app.StosApp

actual fun openBrowser(link: String) {
    val context = StosApp.getContext()
    val intent = CustomTabsIntent.Builder().build().intent.apply {
        data = link.toUri()
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}