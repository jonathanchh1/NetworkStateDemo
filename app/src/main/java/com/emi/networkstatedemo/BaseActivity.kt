package com.emi.networkstatedemo

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.functions.Consumer


open class BaseActivity : AppCompatActivity(){


    override fun onResume() {
        super.onResume()
        NetworkEvent.register(this, Consumer {
            if (it != null) {
                when (it) {
                    NetworkState.NO_INTERNET -> displayErrorDialog(
                        getString(R.string.noInternet),
                        getString(R.string.detail)
                    )
                    NetworkState.NO_RESPONSE -> displayErrorDialog(
                        getString(R.string.noResponse),
                        getString(R.string.noresponse_detail)
                    )
                    NetworkState.UNAUTHORIZED -> {
                        Toast.makeText(this, "verification issue", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                }
            }
        })
    }


    override fun onStop() {
        super.onStop()
        NetworkEvent.unregister(this)
    }


    fun displayErrorDialog(title : String, desc : String){
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(desc)
            .setCancelable(false)
            .setPositiveButton("Ok"){
                dialogInterface, i -> dialogInterface.dismiss()
            }.show()
    }
}