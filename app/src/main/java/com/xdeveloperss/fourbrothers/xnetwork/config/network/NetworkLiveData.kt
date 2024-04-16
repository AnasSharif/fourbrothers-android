package com.xdeveloperss.fourbrothers.xnetwork.config.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

/**
 *@Author: Anas Sharif
 *@Email: anassharif1992@gmail.com
 *@Date: 29/08/2022
 */
object NetworkLiveData :
    LiveData<NetworkInfo>() {

    private lateinit var application: Application
    private lateinit var networkRequest: NetworkRequest

    fun init(application: Application) {
        NetworkLiveData.application = application
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

    override fun onActive() {
        super.onActive()
        CoroutineScope(Dispatchers.IO).launch {
            addValue(execute(), false)
        }
        getDetails()
    }

    private fun getDetails() {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val capabilities = cm.getNetworkCapabilities(network)
                val hasInternetCapability =
                    capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        ?: false
                val hasWifiCapability =
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
                if (hasInternetCapability) {
                    CoroutineScope(Dispatchers.IO).launch {
                        addValue(execute(), hasWifiCapability)
                    }
                } else {
                    addValue(false, hasWifiCapability)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                addValue(isActive = false, isWifi = false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                addValue(isActive = false, isWifi = false)
            }
        })
    }

    fun addValue(isActive: Boolean, isWifi: Boolean) {
        postValue(NetworkInfo(isActive = isActive, isWifi = isWifi))
    }


    fun execute(): Boolean {
        return try {
            Socket().also { socket ->
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                socket.close()
            }
            Log.v("networkInfo", "Network is connect")
            true
        } catch (exception: Exception) {
            Log.v("networkInfo", "Network is connect")
            false
        }
    }
}

