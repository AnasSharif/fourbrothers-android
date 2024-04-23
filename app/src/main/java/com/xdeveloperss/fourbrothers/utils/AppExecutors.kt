package com.xdeveloperss.fourbrothers.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


public class AppExecutors {

    // For Singleton instantiation
    private var LOCK = Any()
    private var sInstance: AppExecutors? = null
    private var diskIO: Executor? = null
    private var mainThread: Executor? = null
    private var networkIO: Executor? = null

    private fun AppExecutors(diskIO: Executor, networkIO: Executor, mainThread: Executor): AppExecutors? {
        this.diskIO = diskIO
        this.networkIO = networkIO
        this.mainThread = mainThread
        return this
    }

    fun getInstance(): AppExecutors? {
        if (sInstance == null) {
            synchronized(LOCK) {
                sInstance = AppExecutors(Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(3),
                    MainThreadExecutor())
            }
        }
        return sInstance
    }

    fun diskIO(): Executor? {
        return diskIO
    }

    fun mainThread(): Executor? {
        return mainThread
    }

    fun networkIO(): Executor? {
        return networkIO
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}