package com.example.americloud.Retrofit

import com.example.frameapp.models.OverlayModel
import com.example.frameapp.models.SignInModel
import com.example.frameapp.models.ToolsModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @POST("auth/sign-in")
    @FormUrlEncoded
    fun signIn(@Field("email") email: String?,
               @Field("password") password: String?, ): Call<SignInModel>?

    @GET("overlays")
    fun getOverlays(@Header("Authorization") token :String? ): Call<OverlayModel>?

    @GET("layers/by-tools")
    fun getTools(@Header("Authorization") token: String?): Call<ToolsModel>?
}