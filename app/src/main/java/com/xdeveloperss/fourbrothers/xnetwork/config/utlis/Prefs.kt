package com.xdeveloperss.fourbrothers.xnetwork.config.utlis

import android.content.Context
import com.xdeveloperss.fourbrothers.xnetwork.config.headers.API_KEY
import com.xdeveloperss.fourbrothers.XBaseApplication

/**
 *@Author: Anas Sharif
 *@Email: anassharif1992@gmail.com
 *@Date: 29/08/2022
 */
object Prefs {

    private var pref = XBaseApplication.xCon().getSharedPreferences(XBaseApplication.xCon().packageName, Context.MODE_PRIVATE)

    fun setToken(token: String) {
        pref.edit().putString(API_KEY, token).apply()
    }
    fun getToken(): String? {
        return pref.getString(API_KEY, null)
    }
}