package com.example.digisapplication.userrepo

import androidx.lifecycle.MutableLiveData
import com.example.digisapplication.Models.ChartResponse
import com.example.digisapplication.Network.MyApi
import com.example.digisapplication.Network.SafeApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class DataRepository (
    private val apiService: MyApi
): SafeApiRequest()  {

    private val addDataResponse= MutableLiveData<ChartResponse>()

    suspend fun getChartData(): MutableLiveData<ChartResponse>? {
        return withContext(Dispatchers.IO) {
            return@withContext datafromApi()
        }
    }

    private suspend fun datafromApi(): MutableLiveData<ChartResponse>? {

        try {
            val response = apiRequest {
                apiService.getChartData()
            }


            addDataResponse.postValue(response)
            return addDataResponse
        } catch (e: Exception) {
            e.printStackTrace()
            debug(e.message+" Casuse:${e.cause}")
        }
        return null
    }
}