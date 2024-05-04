package com.xdeveloperss.fourbrothers

import android.app.Application
import android.content.Context
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

open class XBaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@XBaseApplication)
        }
        GlobalContext.loadKoinModules(listOf(repos, vms, apiModule))

        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.Debug.logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }

    }
    companion object{
        private const val ONESIGNAL_APP_ID = "f8543a3e-f5e5-44fb-9720-0f2294b5dae2"
        private lateinit var instance: XBaseApplication
        fun xCon(): Context {
            return instance
        }
    }

    init {
        instance = this
    }
}