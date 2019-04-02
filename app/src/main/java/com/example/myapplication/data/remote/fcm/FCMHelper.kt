package com.example.myapplication.data.remote.fcm

import com.example.myapplication.data.model.ChatInfo
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FCMHelper{
    @Headers(
        "Authorization: key=AAAAdkbuigY:APA91bFk9_GllZOsCpuuHheOJH9f9gzfr_7Hc0T6nphpjCOwk4L6jAB5gnqn_LvX_iEsA6cmfTO46By-J2a9BppRiqGO5sWYhMIO2bH7RLYUlMQMDAcUGvABuHp4dcBC9sef7GrliWTB\n",
        "Content-Type: application/json"
    )
    @POST("send")
    fun sendMessage(chatInfo:ChatInfo): Single<Response>

    @Headers(
        "Authorization: key=AAAAdkbuigY:APA91bFk9_GllZOsCpuuHheOJH9f9gzfr_7Hc0T6nphpjCOwk4L6jAB5gnqn_LvX_iEsA6cmfTO46By-J2a9BppRiqGO5sWYhMIO2bH7RLYUlMQMDAcUGvABuHp4dcBC9sef7GrliWTB\n",
        "Content-Type: application/json"
    )
    @POST("send")
    fun sendTestMessage(@Body json:JsonObject):Single<ResponseBody>
}