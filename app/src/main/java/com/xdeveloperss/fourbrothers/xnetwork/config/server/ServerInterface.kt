package com.xdeveloperss.fourbrothers.xnetwork.config.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.models.AuthResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
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
    suspend fun login(@Query("username") email: String, @Query("password") pas:String):AuthResponse

    @POST("/api/auth/logout")
    suspend fun logout(): Boolean

    @GET("/api/getData")
    suspend fun getData(@Query("created_at") date: String?=null,
                        @Query("type[]") types: List<String>):BaseResponseRepo
    @Multipart
    @POST("/api/store")
    suspend fun store(@Part("type")type: RequestBody,
                      @Part("itemId")itemId: RequestBody,
                      @Part part: Array<MultipartBody.Part> ):BaseResponseRepo


    companion object {
        val gson: Gson = GsonBuilder()
            .create()
    }
}