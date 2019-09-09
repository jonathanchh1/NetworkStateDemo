package com.emi.networkstatedemo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class ConnectivityState {

    companion object {

        fun isConnected(context: Context) : ConnectivityMode{
            val manaager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = manaager.activeNetwork
            val capabilities = manaager.getNetworkCapabilities(activeNetwork)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                capabilities!!.run {
                    return when{
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectivityMode.WIFI
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectivityMode.CELLULAR
                        else -> ConnectivityMode.NONE
                    }
                }
            }
            return ConnectivityMode.NONE
        }
    }
}