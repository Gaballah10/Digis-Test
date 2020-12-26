package com.example.digisapplication.viewmodefac

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digisapplication.userrepo.DataRepository
import com.example.digisapplication.viewmodel.MainActivityViewModel


class MainActivityModelFactory(
    private val repository: DataRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}