package com.m4ykey.stos.core.network

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openBrowser(link: String) {
    val nsUrl = NSURL.URLWithString(link)
    if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
        UIApplication.sharedApplication.openURL(
            nsUrl,
            emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}