package com.example.frameapp.controller

import com.example.frameapp.Retrofit.WebAPI
import com.example.frameapp.models.OverlayModel
import com.example.frameapp.models.SignInModel
import com.example.frameapp.models.ToolsModel
import retrofit2.Callback
import retrofit2.Response
import retrofit.Retrofit
import retrofit2.Call

class Controller {

    var webAPI: WebAPI? = null
    var signInAPI : SignInAPI? = null
    var getOverlaysAPI : GetOverlaysAPI? = null
    var getToolsAPI : GetToolsAPI? = null

    fun Controller(signIn: SignInAPI) {
        signInAPI = signIn
        webAPI = WebAPI()
    }

    fun Controller(getOverlays: GetOverlaysAPI,getTools: GetToolsAPI)
    {
        getOverlaysAPI = getOverlays
        getToolsAPI = getTools
        webAPI = WebAPI()
    }


    fun setSignIn(email:String?,password:String?)
    {
       webAPI?.api?.signIn(email,password)?.enqueue(object :Callback<SignInModel>{
           override fun onResponse( call: Call<SignInModel>,response: Response<SignInModel>?) {
              val res = response
               signInAPI?.onSuccess(res!!)
           }

           override fun onFailure(call: Call<SignInModel>, t: Throwable) {
              signInAPI?.onError(t.message)
           }

       })
    }

    fun getOverLays(token:String?)
    {
        webAPI?.api?.getOverlays(token)?.enqueue(object :Callback<OverlayModel> {
            override fun onResponse(call: Call<OverlayModel>, response: Response<OverlayModel>) {
                val res = response
                getOverlaysAPI?.onSuccess(res)
            }

            override fun onFailure(call: Call<OverlayModel>, t: Throwable) {
                getOverlaysAPI?.onError(t.message)
            }

        })
    }

    fun getTools(token: String?)
    {
        webAPI?.api?.getTools(token)?.enqueue(object : Callback<ToolsModel> {
            override fun onResponse(call: Call<ToolsModel>, response: Response<ToolsModel>) {
                val res = response
                getToolsAPI?.onSuccessTools(res)
            }

            override fun onFailure(call: Call<ToolsModel>, t: Throwable) {
                getToolsAPI?.onError(t.message)
            }
        })
    }



    interface GetToolsAPI {
        fun onSuccessTools(toolsResponse : Response<ToolsModel>)
        fun onError(error: String?)
    }

    interface GetOverlaysAPI {
        fun onSuccess(overlaysResponse:Response<OverlayModel>)
        fun onError(error: String?)
    }

    interface SignInAPI {
        fun onSuccess(signInResponse:Response<SignInModel>)
        fun onError(error:String?)
    }
}

