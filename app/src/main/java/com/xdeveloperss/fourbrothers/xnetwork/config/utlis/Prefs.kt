package com.xdeveloperss.fourbrothers.xnetwork.config.utlis

import android.content.Context
import com.google.gson.Gson
import com.xdeveloperss.fourbrothers.xnetwork.config.headers.API_KEY
import com.xdeveloperss.fourbrothers.XBaseApplication
import com.xdeveloperss.fourbrothers.data.UserData

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
    fun removeToken() {
        pref.edit().remove(API_KEY).apply()
    }

    fun getString(key: String):String {
        return pref.getString(key,"").toString()
    }
    fun putString(key: String, value: String) {
        pref.edit().putString(key,value).apply()
    }

    fun getUserData(): UserData? {
        return Gson().fromJson(pref.getString("userData",""), UserData::class.java)
    }
}