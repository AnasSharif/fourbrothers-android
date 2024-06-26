package com.xdeveloperss.fourbrothers.xnetwork.config.server

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.models.AuthResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
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
    @FormUrlEncoded
    suspend fun login(@Field("username") email: String, @Field("password") pas:String):AuthResponse

    @POST("/api/auth/logout")
    suspend fun logout(): Boolean

    @GET("/api/getData")
    suspend fun getData(@Query("added_at") date: String?=null,
                        @Query("type[]") types: List<String>):BaseResponseRepo
    @POST("/api/saveData")
    @FormUrlEncoded
    suspend fun saveData(@FieldMap params: Map<String, String>):BaseResponseRepo

    @Multipart
    @POST("/api/store")
    suspend fun store(@Part("type")type: RequestBody,
                      @Part("itemId")itemId: RequestBody,
                      @Part part: Array<MultipartBody.Part> ):BaseResponseRepo
    @GET("/api/getData")
    suspend fun getData(@QueryMap queryParams: Map<String, String>,
                        @Query("type[]") types: List<String>): BaseResponseRepo

    companion object {
        val gson: Gson = GsonBuilder()
            .create()
    }
}