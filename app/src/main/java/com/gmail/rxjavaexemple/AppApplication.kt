package com.gmail.rxjavaexemple

import android.app.Application
import android.content.Context

open class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: Context? = null
            private set
    }
}