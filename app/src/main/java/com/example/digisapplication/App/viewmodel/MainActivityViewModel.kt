package com.example.digisapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digisapplication.Models.ChartResponse
import com.example.digisapplication.userrepo.DataRepository
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainActivityViewModel (var chartDAtaRepository: DataRepository) : ViewModel() {

    fun getData(): Lazy<Deferred<MutableLiveData<ChartResponse>?>> {
        return lazy {
            GlobalScope.async(start = CoroutineStart.LAZY) {
                chartDAtaRepository.getChartData()
            }
        }
    }
}