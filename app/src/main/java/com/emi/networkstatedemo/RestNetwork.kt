package com.emi.networkstatedemo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestNetwork constructor(private val context : Context){

    private fun gsonBuilder() : Gson = GsonBuilder()
        .setLenient()
        .setPrettyPrinting()
        .create()

    private fun getHttpClient() : OkHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addNetworkInterceptor(NetworkInterceptor(context))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY})
        .build()


  private  fun retrofitProvider() : Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getHttpClient())
        .build()


    fun networkProvider() : NetworkServices{
        return retrofitProvider().create(NetworkServices::class.java)
    }


    companion object{
        const val url = "https://www.thecocktaildb.com/api/json/v1/1/"
    }
}