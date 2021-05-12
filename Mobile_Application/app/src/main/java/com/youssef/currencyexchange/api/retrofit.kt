package com.youssef.currencyexchange.api

import com.youssef.currencyexchange.api.model.ExchangeRates
import com.youssef.currencyexchange.api.model.Token
import com.youssef.currencyexchange.api.model.Transaction
import com.youssef.currencyexchange.api.model.User
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


object ExchangeService {
    private const val API_URL: String = "http://10.0.2.2:5000"
    fun exchangeApi():Exchange {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        // Create an instance of our Exchange API interface.
        return retrofit.create(Exchange::class.java);
    }
    interface Exchange {
        @GET("/exchangeRate")
        fun getExchangeRates(): Call<ExchangeRates>

        @POST("/transaction")
        fun addTransaction(@Body transaction: Transaction, @Header("Authorization") authorization: String?): Call<Any>

        @POST("/user")
        fun addUser(@Body user: User): Call<User>

        @POST("/authentication")
        fun authenticate(@Body user: User): Call<Token>

        @GET("/transaction")
        fun getTransactions(@Header("Authorization") authorization: String): Call<List<Transaction>>
    }
}