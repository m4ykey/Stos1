package com.m4ykey.stos.app

import android.app.Application
import android.content.Context
import com.m4ykey.stos.di.initModules
import org.koin.android.ext.koin.androidContext

class StosApp : Application() {

    companion object {
        private var appContext : Context? = null

        fun getContext() : Context {
            return appContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        initModules {
            androidContext(this@StosApp)
        }
    }
}