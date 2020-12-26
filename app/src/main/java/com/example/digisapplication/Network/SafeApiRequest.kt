package com.example.digisapplication.Network

import android.util.Log
import com.example.digisapplication.UTILS.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()
        if(response.isSuccessful){
//            debug("response:${response.body()}")
            return response.body()!!
        }else{
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let{
                try{
                    message.append(JSONObject(it).getString("message"))
                }catch(e: JSONException){ }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            debug("API Error:${message.toString()}")
            throw ApiException(message.toString())
        }
    }

    fun Any?.debug(message: String?){
        Log.d("NozolLogOkTest","$message")
    }
}