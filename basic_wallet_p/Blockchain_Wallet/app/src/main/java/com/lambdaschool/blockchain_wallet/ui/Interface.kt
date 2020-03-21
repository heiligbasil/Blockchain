package com.lambdaschool.blockchain_wallet.ui

import retrofit2.Call
import retrofit2.http.GET

interface Interface {
    @GET("chain")
    fun getData(): Call<Blockchain>
}