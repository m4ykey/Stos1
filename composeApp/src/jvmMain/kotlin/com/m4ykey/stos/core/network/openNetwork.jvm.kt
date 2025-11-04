package com.m4ykey.stos.core.network

import java.awt.Desktop
import java.net.URI

actual fun openBrowser(link: String) {
    if (Desktop.isDesktopSupported()) {
        val desktop = Desktop.getDesktop()
        val uri = URI(link)
        desktop.browse(uri)
    }
}