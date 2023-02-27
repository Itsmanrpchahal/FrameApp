package com.example.frameapp.constants

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

public open class BaseClass : AppCompatActivity() {
    var alertDialog: AlertDialog? = null
    var context: Context? = null
    var sharedPreferences: SharedPreferences? = null
    var welCOmeSharePref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var editor1: SharedPreferences.Editor? = null
    @SuppressLint("CommitPrefEdits")
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        welCOmeSharePref = getSharedPreferences("welcomePref", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        editor1 = welCOmeSharePref?.edit()
    }

    fun showErrorDialog() {
        if (!isFinishing) {
            alertDialog?.show()
        }
    }

    fun setStringVal(key: String?, `val`: String?) {
        editor!!.putString(key, `val`)
        editor!!.apply()
    }

    fun setWelComeString(key: String?, `val`: String?) {
        editor1!!.putString(key, `val`)
        editor1!!.apply()
    }

    fun clearStringVal(key: String?) {
        editor!!.clear()
        editor!!.apply()
    }

    fun getStringVal(key: String?): String? {
        return sharedPreferences!!.getString(key, "")
    }

    fun getWelComeString(key: String?): String? {
        return welCOmeSharePref!!.getString(key, "")
    }
}