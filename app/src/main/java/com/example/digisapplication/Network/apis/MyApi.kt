package com.example.digisapplication.Network

import com.example.digisapplication.Models.ChartResponse
import com.example.digisapplication.UTILS.Constants
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MyApi {

    @GET("random")
   suspend fun getChartData(
    ): Response<ChartResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor? = null
        ): MyApi {

            return Retrofit.Builder()
                .baseUrl(Constants.DataBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}