package com.xdeveloperss.fourbrothers.xnetwork.config

import com.xdeveloperss.fourbrothers.xnetwork.config.headers.ACCEPT
import com.xdeveloperss.fourbrothers.xnetwork.config.headers.AUTHORIZATION
import com.xdeveloperss.fourbrothers.xnetwork.config.headers.BEARER
import com.xdeveloperss.fourbrothers.xnetwork.config.headers.JSON
import com.xdeveloperss.fourbrothers.xnetwork.config.server.ServerInterface
import com.xdeveloperss.fourbrothers.xnetwork.config.utlis.Prefs
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *@Author: Anas Sharif
 *@Email: anassharif1992@gmail.com
 *@Date: 29/08/2022
 */
fun <Api> configureApi(
    api:Class<Api>,
    baseUrl:String
):Api{

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().also { client ->
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            client.addInterceptor(logging)
        }.addInterceptor {
            val newRequest: Request = it.request().newBuilder()
                .addHeader(AUTHORIZATION," $BEARER${Prefs.getToken()}")
                .addHeader(ACCEPT, JSON)
                .build()
            return@addInterceptor it.proceed(newRequest)
        }.build())
        .addConverterFactory(GsonConverterFactory.create(ServerInterface.gson))
        .build()
        .create(api)

}