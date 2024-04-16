package com.xdeveloperss.fourbrothers.xnetwork.config.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 *@Author: Anas Sharif
 *@Email: anassharif1992@gmail.com
 *@Date: 29/08/2022
 */

interface ServerInterface {

    @POST("/api/auth/login")
    suspend fun login(@Query("email") email: String, @Query("password") pas:String)

    @POST("/api/auth/logout")
    suspend fun logout(): Boolean


    companion object {
        val gson: Gson = GsonBuilder()
            .create()
    }
}