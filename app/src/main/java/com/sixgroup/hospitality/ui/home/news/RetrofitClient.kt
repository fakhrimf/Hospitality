package com.sixgroup.hospitality.ui.home.news

import com.sixgroup.hospitality.utils.NEWS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        fun getClient(): Retrofit {
            val baseUrl =
                "https://newsapi.org/v2/"
            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}