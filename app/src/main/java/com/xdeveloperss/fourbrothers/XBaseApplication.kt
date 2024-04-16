package com.xdeveloperss.fourbrothers

import android.app.Application
import android.content.Context
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

    }
    companion object{
        private lateinit var instance: XBaseApplication
        fun xCon(): Context {
            return instance
        }
    }

    init {
        instance = this
    }
}