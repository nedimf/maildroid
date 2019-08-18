package co.nedim.maildroidx

import android.content.Context
import java.net.InetAddress
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import java.net.UnknownHostException


object InternetService {


    /**
     * isNetworkAvailable function is checking if device is connected to internet
     * isInternetAvailable function is pinging google.com to check if there is response ,if internet is available
     */

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    fun isInternetAvailable(context: Context): Boolean {

        return if(isNetworkAvailable(context)) {
            try {
                val ipAddress = InetAddress.getByName("google.com")
                Log.d("InternetService","Network available")

                !ipAddress.equals("")

            } catch (e: UnknownHostException) {
                Log.d("InternetService","Network is not available")
                false

            }
        }else {
            Log.d("InternetService","Network is not available")
            false

        }
    }
}