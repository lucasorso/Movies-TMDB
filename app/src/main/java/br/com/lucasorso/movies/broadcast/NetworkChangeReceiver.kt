package br.com.lucasorso.movies.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.lucasorso.movies.ui.connectionTypeAvailabe

const val NETWORK_STATUS_NOT_CONNECTED = 0
const val NETWORK_STAUS_WIFI = 1
const val NETWORK_STATUS_MOBILE = 2

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w(TAG, "Connection Change")
        var status = context?.connectionTypeAvailabe()
        when (status) {
            NETWORK_STATUS_MOBILE, NETWORK_STAUS_WIFI -> {
                listener.onConnectionChange(true)
            }
            NETWORK_STATUS_NOT_CONNECTED -> {
                listener.onConnectionChange(false)
            }
        }
    }

    companion object {
        val TAG: String = NetworkChangeReceiver::class.java.simpleName

        private lateinit var listener: Listener

        fun setConnectivityListener(listener: Listener) {
            this.listener = listener
        }
    }

    interface Listener {
        fun onConnectionChange(hasInternet: Boolean)
    }
}

