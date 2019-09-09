package com.emi.networkstatedemo

import android.content.Context
import android.util.Log
import com.emi.networkstatedemo.ConnectivityState.Companion.isConnected
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception
import java.net.UnknownHostException

class NetworkInterceptor(private val context: Context) : Interceptor{

    private val networkEvent : NetworkEvent = NetworkEvent
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response : Response?=null

        if(isConnected(context) == ConnectivityMode.NONE){
            networkEvent.publish(NetworkState.NO_INTERNET)
        }else{
            try {
                 response = chain.proceed(request)
                Log.d(NetworkInterceptor::class.java.simpleName, "$response network")
                when(response.code){
                    401 -> networkEvent.publish(NetworkState.UNAUTHORIZED)
                    503 -> networkEvent.publish(NetworkState.NO_RESPONSE)
                }
               return response
            }catch (e : Exception){
                networkEvent.publish(NetworkState.NO_RESPONSE)
                e.printStackTrace()
            }
        }
        return response!!
    }
}