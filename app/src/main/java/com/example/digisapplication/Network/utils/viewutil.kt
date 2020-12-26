package com.example.digisapplication.Network.utils

import android.view.View


fun View?.add(){
    this?.visibility= View.VISIBLE
}
fun View?.remove(){
    this?.visibility= View.GONE
}