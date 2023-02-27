package com.example.frameapp

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.example.frameapp.constants.BaseClass
import com.example.frameapp.constants.Constants
import com.example.frameapp.controller.Controller
import com.example.frameapp.models.SignInModel
import retrofit.Response

class MainActivity : BaseClass() ,Controller.SignInAPI{

    lateinit var newProject_bt : Button
    private lateinit var pd: ProgressDialog
    lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = Controller()
        controller.Controller(this)

        pd = ProgressDialog(this)
        pd!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        pd!!.isIndeterminate = true
        pd!!.setCancelable(false)
        pd.show()
        pd.setContentView(R.layout.loading)

        controller.setSignIn("manpreet@gpcoders.com","Admin@12345")
        findIds()
        listeners()
    }

    private fun listeners() {
        newProject_bt.setOnClickListener {
            var intent = Intent(this,NewProjectScreen::class.java)
            startActivity(intent)
        }
    }

    private fun findIds() {
        newProject_bt = findViewById(R.id.newProject_bt)
    }


    override fun onSuccess(signInResponse: retrofit2.Response<SignInModel>) {
        pd.dismiss()
        if (signInResponse.isSuccessful)
        {
            if (signInResponse.code() == 200)
            {
                setStringVal(Constants.TOKEN,signInResponse.body()?.accessToken)
            }
        }
    }

    override fun onError(error: String?) {
        pd.dismiss()
        Toast.makeText(this,""+error,Toast.LENGTH_SHORT).show()
    }
}