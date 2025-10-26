package com.m4ykey.stos.app

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.m4ykey.stos.app.navigation.AppNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Scaffold(
        contentWindowInsets = WindowInsets.safeGestures
    ) {
        MaterialTheme {
            val navHostController = rememberNavController()
            AppNavHost(navHostController = navHostController)
        }
    }
}